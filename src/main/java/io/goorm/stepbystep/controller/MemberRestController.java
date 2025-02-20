package io.goorm.stepbystep.controller;

import io.goorm.stepbystep.dto.HomeResponse;
import io.goorm.stepbystep.dto.WithdrawalRequest;
import io.goorm.stepbystep.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawalRequest request,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        memberService.withdrawMember(userDetails.getUsername(), request);
        return ResponseEntity.ok().body(new HomeResponse("Successfully withdrawn"));
    }
}