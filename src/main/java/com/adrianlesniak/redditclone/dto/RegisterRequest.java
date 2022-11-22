package com.adrianlesniak.redditclone.dto;

import com.adrianlesniak.redditclone.service.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String username;

    @Password
    private String password;
}
