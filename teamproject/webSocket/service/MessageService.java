package com.eipulse.teamproject.webSocket.service;

import com.eipulse.teamproject.webSocket.entity.MessageEntity;
import com.eipulse.teamproject.webSocket.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private  SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendToUser(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(messageEntity.getReceiver()),
                "/chat/contact",
                messageEntity
        );
    }

    public Page<MessageEntity> getMsg(MessageEntity messageEntity, Pageable pageable){
        return messageRepository.findMsg(messageEntity.getUser1(), messageEntity.getUser2(),pageable);
    }

    public List<MessageEntity> getUser(String my){
        return messageRepository.findUsers(my);
    }

}
