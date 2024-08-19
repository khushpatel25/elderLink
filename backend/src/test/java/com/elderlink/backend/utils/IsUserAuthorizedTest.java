package com.elderlink.backend.utils;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.exceptions.UserIsNotAuthorizedException;
import com.elderlink.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IsUserAuthorizedTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private IsUserAuthorized isUserAuthorized;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void checkUserAuthority_AuthenticatedAndAuthorized() {
        Long userId = 1L;
        String userEmail = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userEmail);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        assertDoesNotThrow(() -> isUserAuthorized.checkUserAuthority(userId));
    }

    @Test
    void checkUserAuthority_NotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> isUserAuthorized.checkUserAuthority(1L));
    }

    @Test
    void checkUserAuthority_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> isUserAuthorized.checkUserAuthority(1L));
    }

    @Test
    void checkUserAuthority_UnauthorizedAccess() {
        Long userId = 1L;
        String userEmail = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("other@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        assertThrows(UserIsNotAuthorizedException.class, () -> isUserAuthorized.checkUserAuthority(userId));
    }
}
