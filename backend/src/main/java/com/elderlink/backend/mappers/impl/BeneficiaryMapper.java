package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.BeneficiaryDto;
import com.elderlink.backend.domains.entities.BeneficiaryEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeneficiaryMapper implements Mapper<BeneficiaryEntity, BeneficiaryDto>{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BeneficiaryEntity toEntity(BeneficiaryDto beneficiaryDto) {
        return modelMapper.map (beneficiaryDto, BeneficiaryEntity.class);
    }

    @Override
    public BeneficiaryDto toDto(BeneficiaryEntity beneficiaryEntity) {
        return modelMapper.map (beneficiaryEntity, BeneficiaryDto.class);
    }
}

