package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.ReviewDto;
import com.elderlink.backend.domains.entities.ReviewEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<ReviewEntity, ReviewDto>{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReviewEntity toEntity(ReviewDto reviewDto) {
        return modelMapper.map(reviewDto, ReviewEntity.class);
    }

    @Override
    public ReviewDto toDto(ReviewEntity reviewEntity) {
        return modelMapper.map(reviewEntity, ReviewDto.class);
    }
}
