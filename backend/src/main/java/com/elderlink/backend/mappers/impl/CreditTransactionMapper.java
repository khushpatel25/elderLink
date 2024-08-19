package com.elderlink.backend.mappers.impl;

import com.elderlink.backend.domains.dto.CreditTransactionDto;
import com.elderlink.backend.domains.entities.CreditTransactionEntity;
import com.elderlink.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditTransactionMapper implements Mapper<CreditTransactionEntity, CreditTransactionDto>{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CreditTransactionEntity toEntity(CreditTransactionDto creditTransactionDto) {
        return modelMapper.map(creditTransactionDto, CreditTransactionEntity.class);
    }

    @Override
    public CreditTransactionDto toDto(CreditTransactionEntity creditTransactionEntity) {
        return modelMapper.map(creditTransactionEntity, CreditTransactionDto.class);
    }
}

