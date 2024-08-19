package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.MessageDto;
import com.elderlink.backend.domains.entities.MessageEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements Mapper<MessageEntity, MessageDto>{

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public MessageEntity toEntity(MessageDto messageDto) {
        return modelMapper.map (messageDto, MessageEntity.class);
    }
    @Override
    public MessageDto toDto(MessageEntity messageEntity) {
        return modelMapper.map (messageEntity, MessageDto.class);
    }
}
