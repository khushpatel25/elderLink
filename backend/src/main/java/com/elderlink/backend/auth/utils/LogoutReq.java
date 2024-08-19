package com.elderlink.backend.auth.utils;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LogoutReq {

    @NotBlank(message = "RefreshToken is required.")
    private String refreshToken;

}
