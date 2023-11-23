package com.eipulse.teamproject.repository.formapprovalrepository;
import com.eipulse.teamproject.entity.formapproval.Chats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ChatsRepository extends JpaRepository<Chats, Integer> {

    @Query(value="from Chats where roomId = ?1 ORDER BY chatsId DESC")
    Page<Chats> findRoom(Integer id, Pageable pageable);

}
