package com.elderlink.backend.repositories;

import com.elderlink.backend.domains.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

}
