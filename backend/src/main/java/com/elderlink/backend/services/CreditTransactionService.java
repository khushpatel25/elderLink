package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.CreditTransactionEntity;

import java.util.List;

public interface CreditTransactionService {

    public CreditTransactionEntity createCreditTransaction(CreditTransactionEntity creditTransactionEntity);
    public List<CreditTransactionEntity> getTransactionBySenderId(Long senderId);

    public List<CreditTransactionEntity> getTransactionRecipientId(Long recipientId);
}
