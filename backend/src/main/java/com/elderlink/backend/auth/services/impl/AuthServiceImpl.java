package com.elderlink.backend.auth.services.impl;

import com.elderlink.backend.auth.services.AuthService;
import com.elderlink.backend.auth.services.JwtService;
import com.elderlink.backend.auth.services.RefreshTokenService;
import com.elderlink.backend.auth.utils.AuthReq;
import com.elderlink.backend.auth.utils.AuthRes;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.domains.enums.UserType;
import com.elderlink.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final int ageOfConsent = 18;
    private static final int retirementAge = 60;
    private static final int  multiPlicationFactor = 10;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @param userReq The user entity containing registration details
     * @return An authentication response containing access and refresh tokens
     */
    @Override
    public AuthRes userRegister(UserEntity userReq) {
        try {

            UserType userType = null;

            Period period = Period.between(userReq.getBirthDate(), LocalDate.now());

            int userAge = period.getYears();
            int pointsToAllocate = (userAge - ageOfConsent ) * multiPlicationFactor;

            if(userAge>=retirementAge){
                userType=UserType.ELDER_PERSON;
            }

            if(userAge<retirementAge){
                userType=UserType.VOLUNTEER;
            }

            //allocate creditBalance based on user's age
            if(userAge>ageOfConsent){
                userReq.setCreditBalance(BigDecimal.valueOf(pointsToAllocate));
            }else{
                userReq.setCreditBalance(BigDecimal.valueOf(0));
            }

            UserEntity user = UserEntity.builder()
                    .firstName(userReq.getFirstName())
                    .lastName(userReq.getLastName())
                    .password(passwordEncoder.encode(userReq.getPassword()))
                    .userType(userType)
                    .phone(userReq.getPhone())
                    .birthDate(userReq.getBirthDate())
                    .address(userReq.getAddress())
                    .creditBalance(userReq.getCreditBalance())
                    .email(userReq.getEmail())
                    .build();

            userRepository.save(user);

            logger.info("User registered successfully : {}", user.getEmail());

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
            return AuthRes.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

        }catch (Exception e){
            logger.error("An error occurred while registering the user.");
            throw new RuntimeException("An error occurred while registering the user. -> " + e.getMessage());
        }
    }
    /**
     * Authenticates a user.
     *
     * @param authReq The authentication request containing user credentials
     * @return An authentication response containing access and refresh tokens
     */

    @Override
    public AuthRes userAuth(AuthReq authReq) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authReq.getEmail(),
                            authReq.getPassword()
                    )
            );
            var user = userRepository.findByEmail(authReq.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

            logger.info("User authenticated successfully : {}", user.getEmail());

            return AuthRes.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
        }catch (Exception e){
            logger.error("An error occurred while authenticating the user. -> {}",e.getMessage());
            throw new RuntimeException("An error occurred while authenticating the user. -> " + e.getMessage());
        }
    }
}
