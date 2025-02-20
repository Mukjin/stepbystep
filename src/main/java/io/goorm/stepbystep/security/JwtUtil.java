package io.goorm.stepbystep.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
    // JWT 토큰 서명에 사용할 키
    private final Key key;
    // 토큰 만료 시간
    private final long expiration;

    // 생성자: application.properties에서 설정값을 주입받음
    public JwtUtil(
            @Value("${jwt.secret}") String secret,  // JWT 비밀키
            @Value("${jwt.expiration}") long expiration) {  // 토큰 만료 시간
        // 비밀키를 바이트 배열로 변환하여 HMAC-SHA 알고리즘에 사용할 키 생성
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // UserDetails 객체로부터 JWT 토큰 생성
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();  // 추가적인 클레임을 담을 맵
        return createToken(claims, userDetails.getUsername());  // 토큰 생성
    }

    // 실제 토큰을 생성하는 private 메소드
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)  // 클레임 정보 설정
                .setSubject(subject)  // 토큰 제목(일반적으로 사용자 이름)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)  // HS256 알고리즘으로 키를 사용하여 서명
                .compact();  // 토큰 생성
    }

    // 토큰의 유효성 검증
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱 시도
            Jwts.parserBuilder()
                    .setSigningKey(key)  // 서명 키 설정
                    .build()
                    .parseClaimsJws(token);  // 토큰 파싱
            return true;  // 파싱 성공시 true 반환
        } catch (JwtException | IllegalArgumentException e) {
            // 파싱 실패시(토큰이 유효하지 않은 경우) 로그 기록 후 false 반환
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 사용자명 추출
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  // 서명 키 설정
                .build()
                .parseClaimsJws(token)  // 토큰 파싱
                .getBody()  // 클레임 바디 가져오기
                .getSubject();  // Subject(사용자명) 추출
    }
}