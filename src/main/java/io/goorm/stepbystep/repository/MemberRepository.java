package io.goorm.stepbystep.repository;

import io.goorm.stepbystep.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberName(String memberName);
}
