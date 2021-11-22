package com.acme.webserviceserentcar.client.api;

import com.acme.webserviceserentcar.client.domain.model.entity.Comment;
import com.acme.webserviceserentcar.client.domain.service.CommentService;
import com.acme.webserviceserentcar.client.mapping.CommentMapper;
import com.acme.webserviceserentcar.client.resource.CommentResource;
import com.acme.webserviceserentcar.client.resource.CreateCommentResource;
import com.acme.webserviceserentcar.client.resource.UpdateCommentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/comments")
public class CommentsController {
    private final CommentService commentService;
    private final CommentMapper mapper;


    public CommentsController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.mapper = commentMapper;
    }

    @Operation(summary = "Get All Comments", description = "Get All Available Comments", tags = {"Comments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Comments returned",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CommentResource.class))
            ))
    })
    @GetMapping
    public Page<CommentResource> getAllComments(Pageable pageable) {
        return mapper.modelListToPage(commentService.getAll(), pageable);
    }

    @Operation(summary = "Get Comment By Id", description = "Get Comment By Id", tags = {"Comments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment returned",
            content = @Content(
                    mediaType = "application/json",
            schema = @Schema(implementation = CommentResource.class)
            ))
    })
    @GetMapping("{id}")
    public CommentResource getCommentById(@PathVariable Long id) {
        return mapper.toResource(commentService.getById(id));
    }

    @Operation(summary = "Create Comment", description = "Create Comment", tags = {"Comments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment was created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentResource.class)
                    ))
    })
    @PostMapping("client/{clientId}/client-comment/{clientCommentId}")
    public CommentResource createComment(@PathVariable Long clientId, @PathVariable Long clientCommentId, @Valid @RequestBody CreateCommentResource request) {
        return mapper.toResource(commentService.create(clientId, clientCommentId, mapper.toModel(request)));
    }

    @Operation(summary = "Update Comment", description = "Update Comment", tags = {"Comments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment was updated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommentResource.class)
            ))
    })
    @PutMapping("{id}")
    public CommentResource updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentResource request) {
        return mapper.toResource(commentService.update(id, mapper.toModel(request)));
    }

    @Operation(summary = "Delete Comment", description = "Delete Comment", tags = {"Comments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment was deleted", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        return commentService.delete(id);
    }
}
