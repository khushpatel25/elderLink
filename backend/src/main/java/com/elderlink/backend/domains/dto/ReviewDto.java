package com.elderlink.backend.domains.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    private Long id;

    @NotNull(message = "VolunteerID is required")
    private Long volunteerId;

    @NotNull(message = "ElderpersonID is required")
    private Long elderPersonId;

    @NotBlank(message = "Review message is required")
    private String reviewMessage;

    @Min (value = 1,message = "Rating should not be less than 1.")
    @Max(value = 5,message = "Rating should not be more than 5.")
    private int rating;

    private LocalDateTime localDateTime;

}

