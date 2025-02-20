package io.goorm.stepbystep.config;

import io.goorm.stepbystep.entity.Member;
import io.goorm.stepbystep.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 일반 사용자 생성
        Member member = new Member();
        member.setMemberName("user");
        member.setPassword(passwordEncoder.encode("1234"));
        member.setRole("ROLE_USER");
        memberRepository.save(member);

        // 관리자 생성
        Member admin = new Member();
        admin.setMemberName("admin");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setRole("ROLE_ADMIN");
        memberRepository.save(admin);
    }
}
