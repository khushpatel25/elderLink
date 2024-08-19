package com.elderlink.backend.auth.services;

import com.elderlink.backend.domains.entities.RefreshTokenEntity;

public interface RefreshTokenService {

    public RefreshTokenEntity createRefreshToken(String userEmail);

    public RefreshTokenEntity verifyRefreshToken(String refreshToken);

    public void deleteRefreshToken(String refreshToken);

}
