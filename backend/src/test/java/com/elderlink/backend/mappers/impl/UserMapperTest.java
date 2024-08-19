package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.UserDto;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.domains.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private UserMapper userMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = new ModelMapper();
        userMapper = new UserMapper();

        // Using reflection to set the modelMapper field in userMapper
        Field field = UserMapper.class.getDeclaredField("modelMapper");
        field.setAccessible(true);
        field.set(userMapper, modelMapper);
    }

    @Test
    public void toDto_CorrectlyMapsUserEntityToUserDto() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("123-456-7890")
                .password("securePassword") // Typically not included in DTO
                .birthDate(LocalDate.of(1980, 5, 20))
                .creditBalance(new BigDecimal("200.00"))
                .userType(UserType.ELDER_PERSON)
                .build();

        UserDto userDto = userMapper.toDto(userEntity);

        assertEquals(userEntity.getId(), userDto.getId());
        assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        assertEquals(userEntity.getLastName(), userDto.getLastName());
        assertEquals(userEntity.getEmail(), userDto.getEmail());
        assertEquals(userEntity.getPhone(), userDto.getPhone());
        assertEquals(userEntity.getBirthDate(), userDto.getBirthDate());
        assertEquals(userEntity.getCreditBalance(), userDto.getCreditBalance());
        assertEquals(userEntity.getUserType(), userDto.getUserType());
    }

    @Test
    public void toEntity_CorrectlyMapsUserDtoToUserEntity() {
        UserDto userDto = UserDto.builder()
                .id(2L)
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@example.com")
                .phone("987-654-3210")
                .password("anotherSecurePassword")
                .birthDate(LocalDate.of(1975, 8, 15))
                .creditBalance(new BigDecimal("150.00"))
                .userType(UserType.VOLUNTEER)
                .build();

        UserEntity userEntity = userMapper.toEntity(userDto);

        assertEquals(userDto.getId(), userEntity.getId());
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getPhone(), userEntity.getPhone());
        assertEquals(userDto.getPassword(), userEntity.getPassword());
        assertEquals(userDto.getBirthDate(), userEntity.getBirthDate());
        assertEquals(userDto.getCreditBalance(), userEntity.getCreditBalance());
        assertEquals(userDto.getUserType(), userEntity.getUserType());
    }
}
