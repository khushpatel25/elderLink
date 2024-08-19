package com.elderlink.backend.domains.dto;

import com.elderlink.backend.domains.enums.RequestCategory;
import com.elderlink.backend.domains.enums.RequestStatus;
import com.elderlink.backend.domains.enums.RequestUrgencyLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
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
public class RequestDto {

    private Long id;

    @NotNull(message = "UserID is required!")
    private Long userId;

    @NotNull(message = "Category is required!")
    @Enumerated(EnumType.STRING)
    private RequestCategory requestCategory;

    @NotNull(message = "Urgency level is required!")
    @Enumerated(EnumType.STRING)
    private RequestUrgencyLevel requestUrgencyLevel;

    @NotBlank(message = "Request description is required!")
    @Size(min = 10,message = "Request description must have at least 10 character!")
    private String requestDescription;

    @NotBlank(message = "Location is required!")
    @Size(min=10, message = "Location must have at least 10 character!")
    private String location;

    @Future
    @NotNull(message = "Date is required!")
    private LocalDate date;

    @NotBlank(message = "Time is required!")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Time must be in the format HH:mm")
    private String time;

    @NotNull(message = "Duration is required!")
    @Positive
    private int durationInMinutes;

    @NotNull(message = "Request status is required!")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private LocalDateTime localDateTime;

    private LocalDateTime updateDateTime;

}
