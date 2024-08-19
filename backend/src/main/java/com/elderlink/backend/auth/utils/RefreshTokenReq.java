package com.elderlink.backend.auth.utils;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenReq {

    @NotBlank(message = "RefreshToken is required.")
    private String refreshToken;

}