package io.goorm.stepbystep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()  // 이 경로들은 모든 사용자에게 허용
                        .requestMatchers("/admin").hasRole("ADMIN")  // ADMIN 역할만 접근 가능
                        .requestMatchers("/home").authenticated()    // 인증된 사용자 접근 가능
                        .anyRequest().authenticated()              // 그 외 모든 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")        // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/")     // 로그인 성공 시 리다이렉트 경로
                        .permitAll()                // 로그인 페이지는 모든 사용자에게 허용
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")      // 로그아웃 성공 시 리다이렉트 경로
                        .permitAll()                // 로그아웃은 모든 사용자에게 허용
                )
                .csrf(csrf -> csrf.disable());  // CSRF 비활성화
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {  // 파라미터로 주입받음
        // 일반 사용자
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();

        // 관리자
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}