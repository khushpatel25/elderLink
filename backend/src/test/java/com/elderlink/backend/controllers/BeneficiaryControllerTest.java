package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.BeneficiaryDto;
import com.elderlink.backend.domains.entities.BeneficiaryEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.BeneficiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class BeneficiaryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BeneficiaryService beneficiaryService;

    @Mock
    private Mapper<BeneficiaryEntity, BeneficiaryDto> beneficiaryMapper;

    @InjectMocks
    private BeneficiaryController beneficiaryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beneficiaryController).build();
    }

    @Test
    void createTransaction_Returns201OnSuccess() throws Exception {
        // Explicitly exclude LocalDateTime from the test DTO and response expectation
        String requestDtoJson = "{\"senderId\": 1,\"recipientId\": 2,\"hoursCredited\": 10}";

        BeneficiaryEntity beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setId(1L);
        beneficiaryEntity.setSender(new UserEntity());
        beneficiaryEntity.setRecipient(new UserEntity());
        beneficiaryEntity.setHoursCredited(new BigDecimal("10"));


        BeneficiaryDto responseDto = new BeneficiaryDto();
        responseDto.setSenderId(1L);
        responseDto.setRecipientId(2L);
        responseDto.setHoursCredited(new BigDecimal("10"));

        when(beneficiaryMapper.toEntity(any(BeneficiaryDto.class))).thenReturn(beneficiaryEntity);
        when(beneficiaryService.createBeneficiary(any(BeneficiaryEntity.class))).thenReturn(beneficiaryEntity);
        when(beneficiaryMapper.toDto(any(BeneficiaryEntity.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/beneficiary/create")
                        .contentType(APPLICATION_JSON)
                        .content(requestDtoJson))
                .andExpect(status().isCreated());
    }

}
