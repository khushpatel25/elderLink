package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.CreditTransactionDto;
import com.elderlink.backend.domains.entities.CreditTransactionEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.services.CreditTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transaction")
public class CreditTransactionsController {

    @Autowired
    private CreditTransactionService creditTransactionService;
    @Autowired
    private Mapper<CreditTransactionEntity, CreditTransactionDto> creditTransactionMapper;
    /**
     * Endpoint to create a new credit transaction.
     *
     * @param creditTransactionDto The DTO containing credit transaction information
     * @return ResponseEntity with status 201 if successful or error status
     */
    @PostMapping("/create")
    public ResponseEntity<CreditTransactionDto> createTransaction(
            @Valid @RequestBody CreditTransactionDto creditTransactionDto
    ){
        CreditTransactionEntity creditTransactionEntity = creditTransactionMapper.toEntity(creditTransactionDto);
        CreditTransactionEntity createdCreditTransaction = creditTransactionService.createCreditTransaction(creditTransactionEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(creditTransactionMapper.toDto(createdCreditTransaction));
    }
    /**
     * Endpoint to retrieve credit transactions by sender ID.
     *
     * @param senderId The ID of the sender
     * @return ResponseEntity containing the list of transactions or error status
     */
    @GetMapping("/getSender/{senderId}")
    public ResponseEntity<List<CreditTransactionDto>> getTransactionBySenderId(
            @Valid @PathVariable("senderId") Long senderId
    ){
        List<CreditTransactionEntity> transactions = creditTransactionService.getTransactionBySenderId(senderId);
        List<CreditTransactionDto> transactionDto = transactions.stream()
                .map(creditTransactionMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionDto);
    }
    /**
     * Endpoint to retrieve credit transactions by recipient ID.
     *
     * @param recipientId The ID of the recipient
     * @return ResponseEntity containing the list of transactions or error status
     */
    @GetMapping("/getRecipient/{recipientId}")
    public ResponseEntity<List<CreditTransactionDto>> getTransactionByRecipientId(
            @Valid @PathVariable("recipientId") Long recipientId
    ){
        List<CreditTransactionEntity> transactions = creditTransactionService.getTransactionRecipientId (recipientId);
        List<CreditTransactionDto> transactionDto = transactions.stream()
                .map(creditTransactionMapper::toDto)
                .collect(Collectors.toList());

        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(transactionDto);

    }
}
