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
@Table(name = "creditTransactions")
public class CreditTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipientId")
    private UserEntity recipient;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private UserEntity sender;

    @OneToOne
    @JoinColumn(name = "requestId")
    private RequestEntity request;

    @Column(nullable = false)
    private BigDecimal hoursCredited;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @PrePersist
    protected void onCreate() {
        localDateTime = LocalDateTime.now();
    }

}

