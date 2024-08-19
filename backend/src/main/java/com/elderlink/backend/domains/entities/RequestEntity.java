package com.elderlink.backend.domains.entities;

import com.elderlink.backend.domains.enums.RequestCategory;
import com.elderlink.backend.domains.enums.RequestStatus;
import com.elderlink.backend.domains.enums.RequestUrgencyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "requests")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestCategory requestCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestUrgencyLevel requestUrgencyLevel;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private int durationInMinutes;

    @Column(nullable = false)
    private String requestDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @Column(nullable = false)
    private LocalDateTime updateDateTime;

    @PrePersist
    protected void onCreate() {
        localDateTime = LocalDateTime.now();
        updateDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDateTime = LocalDateTime.now();
    }
}
