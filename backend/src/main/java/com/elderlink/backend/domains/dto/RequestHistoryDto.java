package com.elderlink.backend.domains.dto;

import com.elderlink.backend.domains.enums.ActionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestHistoryDto {

    private Long id;

    @NotNull(message = "RequestID is required")
    private Long requestId;

    @NotNull(message = "ElderPersonID is required")
    private Long elderPersonId;

    @NotNull(message = "VolunteerID is required")
    private Long volunteerId;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private LocalDateTime localDateTime;

}

