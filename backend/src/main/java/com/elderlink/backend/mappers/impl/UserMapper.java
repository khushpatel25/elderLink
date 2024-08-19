package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.UserDto;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserEntity, UserDto> {

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDto toDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }
    @Override
    public UserEntity toEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
