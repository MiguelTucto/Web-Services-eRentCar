package com.acme.webserviceserentcar.security.domain.model.entity;

import com.acme.webserviceserentcar.security.domain.model.enumeration.Roles;
import com.acme.webserviceserentcar.shared.domain.model.AuditModel;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@With
@AllArgsConstructor
@Entity
public class Role extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Roles name;
}
