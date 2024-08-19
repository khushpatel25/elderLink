package com.elderlink.backend.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private UserEntity receiver;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private UserEntity sender;

    @Column(nullable = false)
    private String messageContent;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @PrePersist
    protected void onCreate() {
        localDateTime = LocalDateTime.now();
    }

}

