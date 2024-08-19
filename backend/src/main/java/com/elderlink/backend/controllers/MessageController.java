package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.MessageDto;
import com.elderlink.backend.domains.entities.MessageEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController{

    @Autowired
    private MessageService messageService;

    @Autowired
    private Mapper<MessageEntity,MessageDto> messageMapper;
    /**
     * Endpoint to create a new message.
     *
     * @param messageDto The DTO containing message information
     * @return ResponseEntity with status 201 if successful or error status
     */
    @PostMapping("/create")
    public ResponseEntity createMessage(
            @Valid @RequestBody MessageDto messageDto
    ){
        MessageEntity messageEntity  = messageMapper.toEntity(messageDto);
        messageService.createMessage (messageEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build ();
    }
    /**
     * Endpoint to retrieve messages between two users.
     *
     * @param senderId The ID of the message sender
     * @param receiverId The ID of the message receiver
     * @return ResponseEntity containing the list of messages or error status
     */

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageDto>> getMessagesBySenderIdReceiverId(
            @Valid @PathVariable("senderId") Long senderId,
            @Valid @PathVariable("receiverId") Long receiverId
    ){
        List<MessageEntity> messages = messageService.getMessageBySenderIdReceiverId(senderId,receiverId);
        List<MessageDto> messageDto = messages.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(messageDto);

    }

}
