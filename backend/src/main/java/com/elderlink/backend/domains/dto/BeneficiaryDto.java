package com.elderlink.backend.domains.dto;

import jakarta.validation.constraints.NotNull;
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
public class BeneficiaryDto{

    private Long id;

    @NotNull(message = "RecipientID is required")
    private Long recipientId;

    @NotNull(message = "senderId is required")
    private Long senderId;

    @NotNull(message = "HoursCredited is required")
    private BigDecimal hoursCredited;

    private LocalDateTime localDateTime;

}
