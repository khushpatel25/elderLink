package com.elderlink.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "beneficiary")
public class BeneficiaryEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RecipientID")
    private UserEntity recipient;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private UserEntity sender;

    @Column(nullable = false)
    private BigDecimal hoursCredited;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @PrePersist
    protected void onCreate() {
        localDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        localDateTime = LocalDateTime.now();
    }

}

