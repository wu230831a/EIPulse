package com.eipulse.teamproject.entity.formapproval;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Chats")
public class Chats {

    @Id
    @Column(name = "chats_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatsId;

    @Column(name = "Emp_id")
    private String empId;

    @Column(name = "Room_id")
    private Integer roomId;

    @Column(name = "Message")
    private String message;

    @Column(name = "File")
    private String file;

    @Column(name = "user_ip")
    private String userIp;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Created_at")
    private LocalDateTime createdAt;

//    @ManyToOne
//    @JoinColumn(name = "Emp_id", referencedColumnName = "Emp_id", insertable = false, updatable = false)
//    private Employee employee;

//    @PrePersist // 當物件主換成 Persistent 狀態以前，做以下事情
//    public void noCreate() {
//        if (createdAt == null)
//            createdAt = new Date();
//    }

}
