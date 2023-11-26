package com.eipulse.teamproject.webSocket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message_entity")
public class MessageEntity {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;
    private Integer user1;
    private Integer user2;
    @Column(name = "chat")
    private String chat;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Created_at")
    private LocalDateTime createdAt;
    private String file;
    private Integer receiver;
    private Integer sender;

}
