package com.acme.webserviceserentcar.client.service;

import com.acme.webserviceserentcar.client.domain.model.entity.Client;
import com.acme.webserviceserentcar.client.domain.model.entity.Comment;
import com.acme.webserviceserentcar.client.domain.persistence.ClientRepository;
import com.acme.webserviceserentcar.client.domain.persistence.CommentRepository;
import com.acme.webserviceserentcar.client.domain.service.CommentService;
import com.acme.webserviceserentcar.shared.exception.ResourceNotFoundException;
import com.acme.webserviceserentcar.shared.exception.ResourceValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {
    private static final String ENTITY = "Comment";
    private final ClientRepository clientRepository;
    private final CommentRepository commentRepository;
    private final Validator validator;

    public CommentServiceImpl(ClientRepository clientRepository, CommentRepository commentRepository, Validator validator) {
        this.clientRepository = clientRepository;
        this.commentRepository = commentRepository;
        this.validator = validator;
    }


    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Page<Comment> getAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
    }

    @Override
    public Comment create(Long clientId, Long clientCommentId, Comment request) {
        Set<ConstraintViolation<Comment>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY,violations);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));

        request.setClient(client);

        Client clientComment = clientRepository.findById(clientCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientCommentId));

        request.setClientComment(clientComment);

        return commentRepository.save(request);
    }

    @Override
    public Comment update(Long id, Comment request) {
        Set<ConstraintViolation<Comment>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        return commentRepository.findById(id).map(comment ->
                commentRepository.save(comment.withDate(request.getDate())
                        .withStars(request.getStars())
                        .withDate(request.getDate()))).orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return commentRepository.findById(id).map(comment -> {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(ENTITY, id));
    }
}
