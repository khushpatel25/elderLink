package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.AddressEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.exceptions.UserAlreadyExistException;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.utils.IsUserAuthorized;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest{

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private UserEntity user;



    @Mock
    private IsUserAuthorized isUserAuthorized;

    @Mock
    private PasswordEncoder passwordEncoder;



    private final Long userId = 1L;
    private UserEntity userEntity;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {

        MockitoAnnotations.openMocks (this);

        user = mock(UserEntity.class);
        user.setId (anyLong ());
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("user@example.com");
        // Use reflection to inject a real ModelMapper instance into userService
        ModelMapper modelMapper = new ModelMapper();
        Field modelMapperField = UserServiceImpl.class.getDeclaredField("modelMapper");
        modelMapperField.setAccessible(true);
        modelMapperField.set(userService, modelMapper);

    }

    @Test
    public void testGetUserByIdSuccess(){

        when (userRepository.findById (user.getId ())).thenReturn (Optional.of (user));

        assertTrue (userService.getUserById (user.getId ()).isPresent ());
        assertEquals (userService.getUserById (user.getId ()).get (),user);

    }

    @Test
    public void testGetUserByIdFailure(){

        when (userRepository.findById (user.getId ())).thenThrow (new RuntimeException ());

        assertThrows (RuntimeException.class,()-> userService.getUserById (user.getId ()));
    }
    @Test
    void userDoesNotExistByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> userService.isUserExistByEmail("user@example.com"));
    }

    @Test
    void userAlreadyExistsByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        assertThrows(UserAlreadyExistException.class, () -> userService.isUserExistByEmail("user@example.com"));
    }

    @Test
    void updateUser_Successful() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        doNothing().when(isUserAuthorized).checkUserAuthority(userId);

        UserEntity updatedUser = userService.updateUser(userId, new UserEntity());

        assertNotNull(updatedUser);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void updateUser_UpdateAddress_Successfully() {
        AddressEntity newAddress = new AddressEntity();
        newAddress.setCity("New City");

        UserEntity existingUser = new UserEntity();
        existingUser.setAddress(new AddressEntity());

        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setAddress(newAddress);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);
        doNothing().when(isUserAuthorized).checkUserAuthority(userId);

        UserEntity result = userService.updateUser(userId, userToUpdate);

        assertNotNull(result);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void updateUser_UpdatePassword_Successfully() {
        String newPassword = "newPassword";
        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setPassword(newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        doNothing().when(isUserAuthorized).checkUserAuthority(userId);

        UserEntity result = userService.updateUser(userId, userToUpdate);

        assertNotNull(result);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(any(UserEntity.class));
    }
    @Test
    void isUserExisted_ReturnsTrue_WhenUserExists() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        assertTrue(userService.isUserExisted(userId));
        verify(userRepository).existsById(userId);
    }

    @Test
    void isUserExisted_ReturnsFalse_WhenUserDoesNotExist() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertFalse(userService.isUserExisted(userId));
        verify(userRepository).existsById(userId);
    }

    @Test
    void isUserExisted_ThrowsRuntimeException_WhenRepositoryThrowsException() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.isUserExisted(userId));
        assertEquals("An error occurred while checking if user exists or not.", exception.getMessage());
        verify(userRepository).existsById(userId);
    }

    @Test
    void updateUser_ThrowsException_WhenEmailIsUpdated() {
        UserEntity updatedUser = new UserEntity();
        updatedUser.setEmail("newemail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.updateUser(userId, updatedUser));
        assertEquals("User can't update email field!", exception.getMessage());
    }

}


