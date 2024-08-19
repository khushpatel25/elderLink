package com.elderlink.backend.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BlogDto {
    private Long id;

    //    @NotBlank(message = "Blog title is required")
    private String title;

    //    @NotBlank(message = "Blog body is required")
    private String body;

    private Long userId;
}
