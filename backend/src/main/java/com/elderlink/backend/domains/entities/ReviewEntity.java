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
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "volunteerId")
    private UserEntity volunteer;

    @ManyToOne
    @JoinColumn(name = "elderPersonId")
    private UserEntity elderPerson;

    //add OneToOne RequestEntity

    private String reviewMessage;

    @Column(nullable = false)
    private int rating;

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

