package com.acme.webserviceserentcar.security.domain.persistence;

import com.acme.webserviceserentcar.security.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
