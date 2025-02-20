package io.goorm.stepbystep.service;

import io.goorm.stepbystep.entity.Member;
import io.goorm.stepbystep.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + memberName));

        return new org.springframework.security.core.userdetails.User(
                member.getMemberName(),
                member.getPassword(),
                member.isEnabled(),
                true,
                true,
                true,
                Collections.singleton(new SimpleGrantedAuthority(member.getRole()))
        );
    }
}
