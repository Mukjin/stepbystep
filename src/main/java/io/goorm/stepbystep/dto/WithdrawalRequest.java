package io.goorm.stepbystep.dto;


import lombok.Data;

@Data
public class WithdrawalRequest {
    private String password;   // 탈퇴 시 비밀번호 확인용
    private String withdrawalReason;   // 탈퇴 시 비밀번호 확인용
}
