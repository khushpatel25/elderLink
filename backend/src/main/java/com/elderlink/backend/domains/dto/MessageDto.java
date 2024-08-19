package com.elderlink.backend.domains.dto;

import jakarta.validation.constraints.NotBlank;
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
public class MessageDto{

    private Long id;

    @NotNull(message = "SenderID is required")
    private Long senderId;

    @NotNull(message = "ReceiverID is required")
    private Long receiverId;

    @NotBlank(message = "Message is required")
    private String messageContent;

    private LocalDateTime localDateTime;


}
