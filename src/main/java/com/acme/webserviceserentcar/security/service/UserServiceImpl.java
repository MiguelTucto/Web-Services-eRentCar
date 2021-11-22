package com.acme.webserviceserentcar.security.service;

import com.acme.webserviceserentcar.client.domain.model.entity.Client;
import com.acme.webserviceserentcar.client.domain.persistence.ClientRepository;
import com.acme.webserviceserentcar.security.domain.model.entity.Role;
import com.acme.webserviceserentcar.security.domain.model.enumeration.Roles;
import com.acme.webserviceserentcar.security.domain.persistence.RoleRepository;
import com.acme.webserviceserentcar.security.domain.persistence.UserRepository;
import com.acme.webserviceserentcar.security.domain.service.UserService;
import com.acme.webserviceserentcar.security.domain.service.communication.AuthenticateRequest;
import com.acme.webserviceserentcar.security.domain.service.communication.AuthenticateResponse;
import com.acme.webserviceserentcar.security.domain.service.communication.RegisterRequest;
import com.acme.webserviceserentcar.security.domain.service.communication.RegisterResponse;
import com.acme.webserviceserentcar.security.middleware.JwtHandler;
import com.acme.webserviceserentcar.security.middleware.UserDetailsImpl;
import com.acme.webserviceserentcar.security.resource.AuthenticateResource;
import com.acme.webserviceserentcar.security.resource.UserResource;
import com.acme.webserviceserentcar.shared.mapping.EnhancedModelMapper;
import com.acme.webserviceserentcar.security.domain.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtHandler handler;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    EnhancedModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Email not found with email: %s", email)));
        return UserDetailsImpl.build(user);
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticateRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = handler.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            AuthenticateResource resource = mapper.map(userDetails, AuthenticateResource.class);
            resource.setRoles(roles);
            resource.setToken(token);

            AuthenticateResponse response = new AuthenticateResponse(resource);

            return ResponseEntity.ok(response);

        } catch(Exception e) {
            AuthenticateResponse response = new AuthenticateResponse(
                    String.format("An error occurred while authenticating: %s", e.getMessage()));
            return ResponseEntity.badRequest().body(response.getMessage());

        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            AuthenticateResponse response = new AuthenticateResponse("Username is already taken.");
            return ResponseEntity.badRequest()
                    .body(response.getMessage());
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            AuthenticateResponse response = new AuthenticateResponse("Email is already taken.");
            return ResponseEntity.badRequest()
                    .body(response.getMessage());
        }

        try {
            Set<String> rolesStringSet = request.getRoles();
            Set<Role> roles = new HashSet<>();

            if(rolesStringSet == null) {
                roleRepository.findByName(Roles.ROLE_USER)
                        .map(roles::add)
                        .orElseThrow(() -> new RuntimeException("Role not found."));
            } else {
                rolesStringSet.forEach(roleString ->
                        roleRepository.findByName(Roles.valueOf(roleString))
                                .map(roles::add)
                                .orElseThrow(() -> new RuntimeException("Role not found.")));
            }

            logger.info("Roles: {}", roles);

            User user = new User()
                    .withUsername(request.getUsername())
                    .withEmail(request.getEmail())
                    .withPassword(encoder.encode(request.getPassword()))
                    .withRoles(roles);

            User auxUser;
            auxUser = userRepository.save(user);

            UserResource resource = mapper.map(user, UserResource.class);
            RegisterResponse response = new RegisterResponse(resource);

            // Create a client after create a User
            Client client = new Client()
                    .withUser(auxUser);
            clientRepository.save(client);

            return ResponseEntity.ok(response.getResource());

        } catch (Exception e) {
            RegisterResponse response = new RegisterResponse(e.getMessage());
            return ResponseEntity.badRequest().body(response.getMessage());
        }

    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
