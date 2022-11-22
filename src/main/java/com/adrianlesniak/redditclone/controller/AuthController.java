package com.adrianlesniak.redditclone.controller;

import com.adrianlesniak.redditclone.dto.AuthenticationResponse;
import com.adrianlesniak.redditclone.dto.LoginRequest;
import com.adrianlesniak.redditclone.dto.RegisterRequest;
import com.adrianlesniak.redditclone.model.VerificationToken;
import com.adrianlesniak.redditclone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        VerificationToken verificationToken = authService.signup(registerRequest);
        return new ResponseEntity<>(verificationToken.getToken(), HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
