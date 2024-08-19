package com.elderlink.backend.mappers.impl;


import com.elderlink.backend.domains.dto.BlogDto;
import com.elderlink.backend.domains.entities.BlogEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper implements Mapper<BlogEntity, BlogDto> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BlogEntity toEntity(BlogDto blogDto){
        return modelMapper.map(blogDto, BlogEntity.class);
    }

    @Override
    public BlogDto toDto(BlogEntity blogEntity) {
        return modelMapper.map(blogEntity, BlogDto.class);
    }
}