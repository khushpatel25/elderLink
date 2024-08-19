package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.RequestHistoryDto;
import com.elderlink.backend.domains.entities.RequestHistoryEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestHistoryMapper implements Mapper<RequestHistoryEntity, RequestHistoryDto>{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RequestHistoryEntity toEntity(RequestHistoryDto requestHistoryDto) {
        return modelMapper.map(requestHistoryDto,RequestHistoryEntity.class);
    }

    @Override
    public RequestHistoryDto toDto(RequestHistoryEntity requestHistoryEntity) {
        return modelMapper.map(requestHistoryEntity,RequestHistoryDto.class);
    }
}
