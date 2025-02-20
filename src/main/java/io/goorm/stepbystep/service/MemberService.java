package io.goorm.stepbystep.service;


import io.goorm.stepbystep.dto.WithdrawalRequest;
import io.goorm.stepbystep.entity.Member;
import io.goorm.stepbystep.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void withdrawMember(String membername, WithdrawalRequest request) {
        log.debug("Withdraw process start: username={}", membername);

        Member member = memberRepository.findByMemberName(membername)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        log.debug("Member found: {}", member);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            log.debug("Password does not match");
            throw new RuntimeException("Invalid password");
        }
        log.debug("Password matches");

        String reason = (request.getWithdrawalReason() == null || request.getWithdrawalReason().trim().isEmpty())
                ? "본인 요청"
                : request.getWithdrawalReason();

        member.setWithdrawalDate(LocalDateTime.now());
        member.setWithdrawalReason(reason);
        member.setEnabled(false);

        log.debug("Member withdrawal processed with reason: {}", reason);
    }
}