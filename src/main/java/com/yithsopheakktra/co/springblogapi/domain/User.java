package com.yithsopheakktra.co.springblogapi.domain;

import com.yithsopheakktra.co.springblogapi.audit.Auditable;
import com.yithsopheakktra.co.springblogapi.utils.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "users")
@Setter
@Getter
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String profileImageUrl;

    private LocalDate dob;

    private Boolean isVerified = false;

    private Boolean isBlocked = false;

    private String verificationCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Audit> audits;
}
