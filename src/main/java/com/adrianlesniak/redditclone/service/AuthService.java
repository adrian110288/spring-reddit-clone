package com.adrianlesniak.redditclone.service;

import com.adrianlesniak.redditclone.dto.RegisterRequest;
import com.adrianlesniak.redditclone.exception.SpringRedditException;
import com.adrianlesniak.redditclone.model.User;
import com.adrianlesniak.redditclone.model.VerificationToken;
import com.adrianlesniak.redditclone.repository.UserRepository;
import com.adrianlesniak.redditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    private final Validator validator;

    @Transactional
    public void signup(RegisterRequest registerRequest) {

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);

        if (!violations.isEmpty()) {

            String errorMessage =
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("%n"));

            throw new SpringRedditException(errorMessage);
        }


        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

//        mailService.sendEmail(new NotificationEmail(
//                "Please activate your account",
//                registerRequest.getEmail(),
//                "Thank you for signing up! Please click on the below url to activate your account: " + "http://localhost:8080/api/auth/accountVerification/" + token
//        ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }
}