package com.elderlink.backend.services;

import com.elderlink.backend.domains.entities.MessageEntity;

import java.util.List;

public interface MessageService{

    public void createMessage(MessageEntity messageEntity);
    public List<MessageEntity> getMessageBySenderIdReceiverId(Long senderId, Long receiverId);

}
