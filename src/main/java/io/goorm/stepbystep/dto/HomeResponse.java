package io.goorm.stepbystep.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeResponse {
    private String message;
    private LocalDateTime timestamp;

    public HomeResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}