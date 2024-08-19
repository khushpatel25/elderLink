package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.CreditTransactionEntity;
import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.CreditTransactionRepository;
import com.elderlink.backend.services.CreditTransactionService;
import com.elderlink.backend.services.RequestService;
import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CreditTransactionImpl implements CreditTransactionService{

    Logger logger = LoggerFactory.getLogger (CreditTransactionImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private CreditTransactionRepository creditTransactionRepository;
    @Autowired
    private IsUserAuthorized isUserAuthorized;
    /**
     * Create a credit transaction.
     *
     * @param creditTransactionEntity The credit transaction entity to create
     * @return The created credit transaction entity
     */
    @Override
    public CreditTransactionEntity createCreditTransaction(CreditTransactionEntity creditTransactionEntity) {
        try {

            Long senderId = creditTransactionEntity.getSender().getId();
            Long recipientId = creditTransactionEntity.getRecipient().getId();
            Long requestId = creditTransactionEntity.getRequest ().getId ();
            BigDecimal hoursCredited = creditTransactionEntity.getHoursCredited();

            UserEntity senderProfile = userService.getUserById(senderId)
                    .orElseThrow(() -> new EntityNotFoundException ("User with this id doesn't exist!"));
            UserEntity recipientProfile = userService.getUserById(recipientId)
                    .orElseThrow(() -> new EntityNotFoundException("User with this id doesn't exist!"));
            RequestEntity request = requestService.findRequestById (requestId)
                    .orElseThrow (() -> new EntityNotFoundException ("Request with this id doesn't exist!"));

            //To check only sender(ELDER_PERSON) is authorized to make the transaction
            isUserAuthorized.checkUserAuthority(senderId);

            //To check ELDER_PERSON is the one who created this request
            if(!Objects.equals (request.getUser ().getId (), senderId)){
                throw new RuntimeException ("sender is not the one who created this request!");
            }

            //To check sender(ELDER_PERSON) has enough credit to make transaction
            if(senderProfile.getCreditBalance().compareTo(hoursCredited)<0){
                throw new RuntimeException("Sender doesn't have enough credits to make this transaction!");
            }

            //Update the creditsBalance in both sender and recipient profile
            senderProfile.setCreditBalance(senderProfile.getCreditBalance().subtract(hoursCredited));
            recipientProfile.setCreditBalance(recipientProfile.getCreditBalance().add(hoursCredited));

            //Create and return the transaction
            CreditTransactionEntity creditTransaction = creditTransactionRepository.save (creditTransactionEntity);
            logger.info ("Credits transferred successfully.");
            return creditTransaction;
        }catch(EntityNotFoundException e){
            logger.error ("An error occurred while processing the credit transaction! -> {}",e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (Exception e){
            logger.error ("An error occurred while processing the credit transaction! -> {}",e.getMessage ());
            throw new RuntimeException(e.getMessage ());
        }
    }
    /**
     * Get all credit transactions made by a sender.
     *
     * @param senderId The ID of the sender
     * @return List of credit transactions made by the sender
     */
    @Override
    public List<CreditTransactionEntity> getTransactionBySenderId(Long senderId) {
        try{
            if(!userService.isUserExisted (senderId)){
                throw new EntityNotFoundException ("Sender with this id doesn't exist!");
            }
            return creditTransactionRepository.getCreditTransactionBySenderId (senderId);
        }catch (EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (Exception e){
            logger.error (e.getMessage ());
            throw new RuntimeException (e.getMessage ());
        }
    }
    /**
     * Get all credit transactions received by a recipient.
     *
     * @param recipientId The ID of the recipient
     * @return List of credit transactions received by the recipient
     */
    @Override
    public List<CreditTransactionEntity> getTransactionRecipientId(Long recipientId) {
        try{
            if(!userService.isUserExisted (recipientId)){
                throw new EntityNotFoundException ("Recipient with this id doesn't exist!");
            }
            return creditTransactionRepository.getCreditTransactionBySenderId (recipientId);
        }catch (EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (Exception e){
            logger.error (e.getMessage ());
            throw new RuntimeException (e.getMessage ());
        }
    }
}
