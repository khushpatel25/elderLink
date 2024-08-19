package com.elderlink.backend.repositories;

import com.elderlink.backend.domains.entities.CreditTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransactionEntity,Long>{

    List<CreditTransactionEntity> getCreditTransactionBySenderId(Long senderId);

    List<CreditTransactionEntity> getCreditTransactionByRecipientId(Long recipientId);
}
