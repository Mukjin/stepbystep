package io.goorm.stepbystep.dto;

import lombok.Data;

public class AuthDto {

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }
    }
}