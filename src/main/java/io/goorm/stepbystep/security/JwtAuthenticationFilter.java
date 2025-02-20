package io.goorm.stepbystep.security;


// JWT 관련 임포트

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {  // 요청당 한 번만 실행되는 필터

    // JWT 유틸리티 클래스 주입
    private final JwtUtil jwtUtil;

    // 사용자 정보를 가져오는 서비스 주입
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // HTTP 요청 헤더에서 "Authorization" 값을 가져옴
        final String authHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 Bearer 토큰이 아닌 경우 다음 필터로 진행
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 제거하고 실제 JWT 토큰 값만 추출
        String jwt = authHeader.substring(7);
        // JWT 토큰에서 사용자명 추출
        String username = jwtUtil.extractUsername(jwt);

        // 사용자명이 존재하고 현재 SecurityContext에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DB에서 사용자 정보를 가져옴
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰이 유효한 경우
            if (jwtUtil.validateToken(jwt)) {
                // 인증 토큰 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,  // 사용자 정보
                        null,        // 자격증명(보통 비밀번호지만 JWT에서는 불필요)
                        userDetails.getAuthorities()  // 사용자 권한 정보
                );
                // 현재 요청 정보를 인증 토큰에 설정
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}