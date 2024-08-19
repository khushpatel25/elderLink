package com.elderlink.backend.auth.services.impl;

import com.elderlink.backend.auth.services.RefreshTokenService;
import com.elderlink.backend.domains.entities.RefreshTokenEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.RefreshTokenRepository;
import com.elderlink.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final long refreshTokenExpirationTime = 1000 * 60 * 600;
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    /**
     * Creates a new refresh token for the user with the given email.
     *
     * @param userEmail The email of the user
     * @return The created refresh token entity
     */
    @Override
    public RefreshTokenEntity createRefreshToken(String userEmail) {
        try {
            UserEntity user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User with this email doesn't exist!"));

            RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenExpirationTime))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);

            logger.info("Created new refreshToken for user : {}",  userEmail);

            return refreshToken;

        }catch (UsernameNotFoundException e){
            logger.error("User is not found with email : {} ",userEmail);
            throw e;
        }catch(Exception e){
            logger.error("Error occurred while creating refreshToken for user : {}",userEmail);
            throw new RuntimeException("Error occurred while creating refreshToken. -> " + e.getMessage());
        }
    }
    /**
     * Verifies the given refresh token and returns its entity if valid.
     *
     * @param refreshToken The refresh token to verify
     * @return The refresh token entity
     */
    @Override
    public RefreshTokenEntity verifyRefreshToken(String refreshToken) {
       try{
           RefreshTokenEntity refTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                   .orElseThrow(() -> new EntityNotFoundException("RefreshToken isn't found!"));

           if(refTokenEntity.getExpirationTime().compareTo(Instant.now())<0){
               refreshTokenRepository.deleteById(refTokenEntity.getId());
               throw new RuntimeException("RefreshToken is expired!");
           }

           logger.info("Successfully verified refreshToken : {}" , refreshToken);

           return refTokenEntity;
       }catch (EntityNotFoundException e){
            logger.error("RefreshToken not found : {}",refreshToken);
            throw e;
       }catch (Exception e){
           logger.error("Error occurred while verifying the refreshToken : {}",refreshToken);
           throw new RuntimeException("Error occurred while verifying the refreshToken. -> " + e.getMessage());
       }
    }
    /**
     * Deletes the refresh token with the given value.
     *
     * @param refreshToken The refresh token to delete
     */
    @Override
    public void deleteRefreshToken(String refreshToken) {
        try{
            RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new EntityNotFoundException("RefreshToken is not found!"));

            refreshTokenRepository.deleteById(refreshTokenEntity.getId());

            logger.info("RefreshToken deleted successfully : {}",refreshToken);
        }catch (EntityNotFoundException e){
            logger.error("RefreshToken is not found : {}",refreshToken);
            throw e;
        }catch (Exception e){
            logger.error("Error occurred while deleting the refreshToken : {}",refreshToken);
            throw new RuntimeException("Error occurred while deleting the refreshToken. -> " + e.getMessage());
        }
    }
}
