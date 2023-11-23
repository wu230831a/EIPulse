package com.eipulse.teamproject.webSocket.repository;

import com.eipulse.teamproject.webSocket.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    @Query("from MessageEntity where user1 = ?1 and user2 = ?2  ORDER BY messageId DESC")
    Page<MessageEntity> findMsg(Integer user1, Integer user2, Pageable pageable);

//    @Query("select DISTINCT case when user1 = :my THEN user2 ELSE user1 end from MessageEntity where user1 = :my or user2 = :my")
//    List<String> findUsers(String my);

    @Query("SELECT m \n" +
            "FROM MessageEntity m\n" +
            "WHERE m.messageId IN (\n" +
            "  SELECT MAX(messageId) \n" +
            "  FROM MessageEntity\n" +
            "  where user1 = :user or user2 = :user" +
            "  GROUP BY user1, user2\n" +
            ")")
    List<MessageEntity> findUsers(String user);
}
