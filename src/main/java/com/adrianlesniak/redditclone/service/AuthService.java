package com.adrianlesniak.redditclone.service;

import com.adrianlesniak.redditclone.dto.AuthenticationResponse;
import com.adrianlesniak.redditclone.dto.LoginRequest;
import com.adrianlesniak.redditclone.dto.RefreshTokenRequest;
import com.adrianlesniak.redditclone.dto.RegisterRequest;
import com.adrianlesniak.redditclone.exception.SpringRedditException;
import com.adrianlesniak.redditclone.exception.UsernameNotFoundException;
import com.adrianlesniak.redditclone.model.User;
import com.adrianlesniak.redditclone.model.VerificationToken;
import com.adrianlesniak.redditclone.repository.UserRepository;
import com.adrianlesniak.redditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.Principal;
import java.time.Instant;
import java.util.Optional;
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

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public VerificationToken signup(RegisterRequest registerRequest) {

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

        VerificationToken verificationToken = generateVerificationToken(user);

        verificationTokenRepository.save(verificationToken);

//        mailService.sendEmail(new NotificationEmail(
//                "Please activate your account",
//                registerRequest.getEmail(),
//                "Thank you for signing up! Please click on the below url to activate your account: " + "http://localhost:8080/api/auth/accountVerification/" + verificationToken.getToken()
//        ));

        return verificationToken;
    }

    private VerificationToken generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        return verificationToken;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token!"));
        fetchUserAndEnabled(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnabled(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with name " + username + "!"));

        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String token = tokenService.generateToken(authentication);

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(Principal principal) {

        // Instead of injecting Principal everywhere, this can be used
        // SecurityContextHolder.getContext().getAuthentication()

        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getName()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = tokenService.getTokenWithUsername(refreshTokenRequest.getUserName());

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .username(refreshTokenRequest.getUserName())
                .build();
    }
}
