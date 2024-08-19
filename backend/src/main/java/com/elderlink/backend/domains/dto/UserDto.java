package com.elderlink.backend.domains.dto;

import com.elderlink.backend.domains.enums.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid phone number format")
    private String phone;

    @Size(min = 8, message = "Password must have at least 8 characters!")
    private String password;

    @Past
    private LocalDate birthDate;

    private BigDecimal creditBalance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private AddressDto address;

}
