package io.goorm.stepbystep.controller;

// Spring Framework 관련 임포트

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Spring Security 관련 임포트
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

// 프로젝트 내부 클래스 임포트
import io.goorm.stepbystep.security.JwtUtil;
import io.goorm.stepbystep.dto.AuthDto;


// Lombok 임포트
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    // Spring Security의 인증을 담당하는 매니저
    private final AuthenticationManager authenticationManager;
    // JWT 토큰 생성 및 검증을 위한 유틸리티
    private final JwtUtil jwtUtil;

    // POST /api/auth/login 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(@RequestBody AuthDto.LoginRequest loginRequest) {
        // 사용자가 제공한 username과 password로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),  // 요청에서 username 추출
                        loginRequest.getPassword()   // 요청에서 password 추출
                )
        );

        // 인증 성공 시 UserDetails 객체 추출
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // UserDetails를 기반으로 JWT 토큰 생성
        String token = jwtUtil.generateToken(userDetails);

        // 생성된 토큰을 응답으로 반환
        return ResponseEntity.ok(new AuthDto.LoginResponse(token));
    }
}