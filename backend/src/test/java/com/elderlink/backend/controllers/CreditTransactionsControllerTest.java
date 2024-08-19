package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.CreditTransactionDto;
import com.elderlink.backend.domains.entities.CreditTransactionEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.CreditTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CreditTransactionsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreditTransactionService creditTransactionService;

    @Mock
    private Mapper<CreditTransactionEntity, CreditTransactionDto> creditTransactionMapper;

    @InjectMocks
    private CreditTransactionsController creditTransactionsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(creditTransactionsController).build();
    }

    @Test
    void getTransactionBySenderId_Returns200OnSuccess() throws Exception {
        Long senderId = 1L;
        List<CreditTransactionEntity> transactions = Arrays.asList(
                new CreditTransactionEntity(1L, null, null, null, new BigDecimal("10"), null) // Exclude LocalDateTime
        );
        when(creditTransactionService.getTransactionBySenderId(senderId)).thenReturn(transactions);
        when(creditTransactionMapper.toDto(any(CreditTransactionEntity.class))).thenAnswer(invocation -> {
            CreditTransactionEntity entity = invocation.getArgument(0);
            return new CreditTransactionDto(entity.getId(), senderId, null, null, entity.getHoursCredited(), null); // Exclude LocalDateTime
        });

        mockMvc.perform(get("/api/transaction/getSender/{senderId}", senderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderId").value(senderId));
    }

    @Test
    void getTransactionByRecipientId_Returns200OnSuccess() throws Exception {
        Long recipientId = 2L;
        List<CreditTransactionEntity> transactions = Arrays.asList(
                new CreditTransactionEntity(1L, null, null, null, new BigDecimal("5"), null) // Exclude LocalDateTime
        );
        when(creditTransactionService.getTransactionRecipientId(recipientId)).thenReturn(transactions);
        when(creditTransactionMapper.toDto(any(CreditTransactionEntity.class))).thenAnswer(invocation -> {
            CreditTransactionEntity entity = invocation.getArgument(0);
            return new CreditTransactionDto(entity.getId(), null, recipientId, null, entity.getHoursCredited(), null); // Exclude LocalDateTime
        });

        mockMvc.perform(get("/api/transaction/getRecipient/{recipientId}", recipientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipientId").value(recipientId));
    }

}
