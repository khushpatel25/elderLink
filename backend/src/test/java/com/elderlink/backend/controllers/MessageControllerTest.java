package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.MessageDto;
import com.elderlink.backend.domains.entities.MessageEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private Mapper<MessageEntity, MessageDto> messageMapper;

    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void createMessage_Returns201OnSuccess() throws Exception {
        MessageDto messageDto = new MessageDto(null, 1L, 2L, "Hello, World!", null);
        MessageEntity messageEntity = new MessageEntity(null, null, null, "Hello, World!", null);

        // Configure the mapper to return a non-null MessageEntity when toEntity is called.
        when(messageMapper.toEntity(any(MessageDto.class))).thenReturn(messageEntity);

        String jsonPayload = new ObjectMapper().writeValueAsString(messageDto);

        mockMvc.perform(post("/api/messages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());

        // Verify that createMessage was called with any non-null MessageEntity object.
        verify(messageService).createMessage(any(MessageEntity.class));
    }


    @Test
    void getMessagesBySenderIdReceiverId_Returns200OnSuccess() throws Exception {
        Long senderId = 1L;
        Long receiverId = 2L;
        List<MessageEntity> messageEntities = List.of(new MessageEntity(1L, null, null, "Test message", null));
        List<MessageDto> messageDtos = messageEntities.stream()
                .map(entity -> new MessageDto(entity.getId(), senderId, receiverId, entity.getMessageContent(), null))
                .collect(Collectors.toList());

        when(messageService.getMessageBySenderIdReceiverId(senderId, receiverId)).thenReturn(messageEntities);
        when(messageMapper.toDto(any(MessageEntity.class))).thenAnswer(invocation -> messageDtos.get(0));

        mockMvc.perform(get("/api/messages/{senderId}/{receiverId}", senderId, receiverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageContent").value("Test message"));

        verify(messageService).getMessageBySenderIdReceiverId(senderId, receiverId);
    }
}
