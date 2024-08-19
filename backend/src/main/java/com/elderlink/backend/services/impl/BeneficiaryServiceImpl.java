package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.BeneficiaryEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.BeneficiaryRepository;
import com.elderlink.backend.services.BeneficiaryService;
import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService{

    Logger logger = LoggerFactory.getLogger (BeneficiaryServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IsUserAuthorized isUserAuthorized;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Override
    public BeneficiaryEntity createBeneficiary(BeneficiaryEntity beneficiary) {
        try {

            Long senderId = beneficiary.getSender().getId();
            Long recipientId = beneficiary.getRecipient().getId();
            BigDecimal hoursCredited = beneficiary.getHoursCredited();

            UserEntity senderProfile = userService.getUserById(senderId)
                    .orElseThrow(() -> new EntityNotFoundException ("User with this id doesn't exist!"));
            UserEntity recipientProfile = userService.getUserById(recipientId)
                    .orElseThrow(() -> new EntityNotFoundException("User with this id doesn't exist!"));

            //To check only sender(ELDER_PERSON) is authorized to make the transaction
            isUserAuthorized.checkUserAuthority(senderId);

            //To check sender(ELDER_PERSON) has enough credit to make transaction
            if(senderProfile.getCreditBalance().compareTo(hoursCredited)<0){
                throw new RuntimeException("Sender doesn't have enough credits to make this transaction!");
            }

            //Update the creditsBalance in both sender and recipient profile
            senderProfile.setCreditBalance(senderProfile.getCreditBalance().subtract(hoursCredited));
            recipientProfile.setCreditBalance(recipientProfile.getCreditBalance().add(hoursCredited));

            //Create and return the transaction
            BeneficiaryEntity beneficiaryEntity = beneficiaryRepository.save (beneficiary);
            logger.info ("Credits transferred successfully.");
            return beneficiaryEntity;
        }catch(EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (Exception e){
            logger.error (e.getMessage ());
            throw new RuntimeException(e.getMessage ());
        }
    }

}
