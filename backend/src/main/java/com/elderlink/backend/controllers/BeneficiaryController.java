package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.BeneficiaryDto;
import com.elderlink.backend.domains.entities.BeneficiaryEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.BeneficiaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/beneficiary")
public class BeneficiaryController{

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private Mapper<BeneficiaryEntity, BeneficiaryDto> beneficiaryMapper;

    @PostMapping("/create")
    public ResponseEntity<BeneficiaryDto> createTransaction(
            @Valid @RequestBody BeneficiaryDto beneficiaryDto
    ){
        BeneficiaryEntity beneficiaryEntity = beneficiaryMapper.toEntity(beneficiaryDto);
        BeneficiaryEntity beneficiary = beneficiaryService.createBeneficiary (beneficiaryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryMapper.toDto(beneficiary));
    }

}
