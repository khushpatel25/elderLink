package com.elderlink.backend.domains.entities;

import com.elderlink.backend.domains.enums.ActionType;
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
@Table(name = "requestsHistory")
public class RequestHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestId")
    private RequestEntity request;

    @ManyToOne
    @JoinColumn(name = "elderPersonId")
    private UserEntity elderPerson;

    @ManyToOne
    @JoinColumn(name = "volunteerId")
    private UserEntity volunteer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

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
