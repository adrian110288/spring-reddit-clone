package com.adrianlesniak.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
/**
 * When using @JoinTable with referencedColumnName that is not a primary key,
 * then the referenced class needs to be marked Serializable
 *
 * e.g. Subreddit class references User by username (not PK)
 */
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    private Instant created;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;
}
