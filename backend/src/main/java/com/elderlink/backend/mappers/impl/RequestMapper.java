package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.RequestDto;
import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper implements Mapper<RequestEntity, RequestDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RequestDto toDto(RequestEntity requestEntity) {
        return modelMapper.map(requestEntity, RequestDto.class);
    }

    @Override
    public RequestEntity toEntity(RequestDto requestDto) {
        return modelMapper.map(requestDto, RequestEntity.class);
    }
}
