package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.RequestDto;
import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.domains.enums.RequestCategory;
import com.elderlink.backend.domains.enums.RequestStatus;
import com.elderlink.backend.domains.enums.RequestUrgencyLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequestMapperTest {

    @Autowired
    private RequestMapper requestMapper;

    @Test
    void toDto_CorrectlyMapsRequestEntityToRequestDto() {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1L);
        requestEntity.setRequestCategory(RequestCategory.TRANSPORTATION);
        requestEntity.setRequestUrgencyLevel(RequestUrgencyLevel.URGENT);
        requestEntity.setLocation("123 Main St");
        requestEntity.setDate(LocalDate.now());
        requestEntity.setTime("10:00");
        requestEntity.setDurationInMinutes(30);
        requestEntity.setRequestDescription("Need a ride");
        requestEntity.setRequestStatus(RequestStatus.OPEN);
        requestEntity.setLocalDateTime(LocalDateTime.now());
        requestEntity.setUpdateDateTime(LocalDateTime.now());

        RequestDto requestDto = requestMapper.toDto(requestEntity);

        assertEquals(requestEntity.getId(), requestDto.getId());
    }

    @Test
    void toEntity_CorrectlyMapsRequestDtoToRequestEntity() {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setUserId(2L); // Assuming this represents an existing user
        requestDto.setRequestCategory(RequestCategory.TRANSPORTATION);
        requestDto.setRequestUrgencyLevel(RequestUrgencyLevel.URGENT);
        requestDto.setLocation("123 Main St");
        requestDto.setDate(LocalDate.now());
        requestDto.setTime("10:00");
        requestDto.setDurationInMinutes(30);
        requestDto.setRequestDescription("Need a ride");
        requestDto.setRequestStatus(RequestStatus.OPEN);
        requestDto.setLocalDateTime(LocalDateTime.now());
        requestDto.setUpdateDateTime(LocalDateTime.now());

        RequestEntity requestEntity = requestMapper.toEntity(requestDto);

        assertEquals(requestDto.getId(), requestEntity.getId());
        // Add more assertions for each field
    }
}
