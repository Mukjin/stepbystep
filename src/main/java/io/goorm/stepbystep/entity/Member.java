package io.goorm.stepbystep.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberName;

    private String password;

    private String role;

    private boolean enabled = true;

    private LocalDateTime withdrawalDate;
    private String withdrawalReason;

}