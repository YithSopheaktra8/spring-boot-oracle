package com.yithsopheakktra.co.springblogapi.domain;

import com.yithsopheakktra.co.springblogapi.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


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


}
