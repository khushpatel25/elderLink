package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.MessageEntity;
import com.elderlink.backend.repositories.MessageRepository;
import com.elderlink.backend.services.MessageService;
import com.elderlink.backend.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    Logger logger = LoggerFactory.getLogger (MessageServiceImpl.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;
    /**
     * Create a new message.
     *
     * @param messageEntity The message entity to create
     */
    @Override
    public void createMessage(MessageEntity messageEntity) {

        try {

            if(!userService.isUserExisted(messageEntity.getSender ().getId())){
                throw new EntityNotFoundException ("Sender with this id doesn't exist!");
            }

            if(!userService.isUserExisted (messageEntity.getReceiver ().getId ())){
                throw new EntityNotFoundException ("Receiver with this id doesn't exist!");
            }

            messageRepository.save (messageEntity);

            logger.info ("Message created successfully.");
        }catch (EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (Exception e){
            logger.error (e.getMessage ());
            throw new RuntimeException (e.getMessage ());
        }

    }
    /**
     * Get messages by sender ID and receiver ID.
     *
     * @param senderId   The ID of the sender
     * @param receiverId The ID of the receiver
     * @return List of messages sent from sender to receiver
     */

    @Override
    public List<MessageEntity> getMessageBySenderIdReceiverId(Long senderId, Long receiverId) {

        try{
            if(!userService.isUserExisted(senderId)){
                throw new EntityNotFoundException ("Sender with this id doesn't exist!");
            }

            if(!userService.isUserExisted (receiverId)){
                throw new EntityNotFoundException ("Receiver with this id doesn't exist!");
            }

            List<MessageEntity> messages = messageRepository.findBySenderIdAndReceiverId (senderId,receiverId);

            logger.info ("Fetched messages successfully.");

            return messages;
        }catch (EntityNotFoundException e){
            logger.error (e.getMessage ());
            throw new EntityNotFoundException (e.getMessage ());
        }catch (Exception e){
            logger.error (e.getMessage ());
            throw new RuntimeException (e.getMessage ());
        }
    }
}

