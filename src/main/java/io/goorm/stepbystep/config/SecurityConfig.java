package io.goorm.stepbystep.config;

// Spring Security 및 JWT 필터 관련 임포트
import io.goorm.stepbystep.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // JWT 인증 필터 주입
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (JWT를 사용하므로 불필요)
                .csrf(csrf -> csrf.disable())

                // URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 경로 설정
                        .requestMatchers("/api/","/api/auth/**", "/h2-console/**").permitAll()
                        // 관리자 권한이 필요한 경로 설정
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 세션 관리 설정
                .sessionManagement(session -> session
                        // JWT를 사용하므로 세션을 생성하지 않음
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 폼 로그인 비활성화 추가
                .formLogin(formLogin -> formLogin.disable())
                // HTTP Basic 인증도 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // H2 콘솔 사용을 위한 설정
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()  // 같은 출처의 프레임은 허용
                        )
                );

        return http.build();
    }

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 비밀번호 인코더 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 알고리즘 사용
    }
}