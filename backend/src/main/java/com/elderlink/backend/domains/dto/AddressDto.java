package com.elderlink.backend.domains.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

    private Long id;

    @NotBlank(message = "Street name is required")
    private String street_name;

    @NotBlank(message = "Suite number is required")
    private String suite_number;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

}
