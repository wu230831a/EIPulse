package com.eipulse.teamproject.webSocket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSendDTO {
    private byte[] file;
    private MessageEntity messageEntity;
}
