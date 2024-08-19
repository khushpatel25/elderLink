package com.elderlink.backend.auth.utils;

import com.elderlink.backend.domains.dto.AddressDto;
import com.elderlink.backend.domains.enums.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegReq {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters!")
    private String password;

    @Past
    @NotNull(message = "Birthdate is required!")
    private LocalDate birthDate;

    @Valid
    private AddressDto address;

}
