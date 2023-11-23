package com.eipulse.teamproject.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubscriptionEventListener {

    List<String> connectedUsers = new ArrayList<>();
    private final SimpUserRegistry userRegistry;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public SubscriptionEventListener(SimpUserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }
// 監聽訂閱開始
    @EventListener
    public void handleSubscriptionEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String input = accessor.getDestination();
        int startIndex = input.indexOf("/topic/");
        int endIndex = input.lastIndexOf("/connectedUsers");

        if (accessor.getDestination() != null) {
            String destination = accessor.getDestination();
            if (destination.startsWith("/topic/") && destination.contains("/connectedUsers")) {
                // 檢查是否符合規則，例如檢查是否包含特定的roomid等
                if(!connectedUsers.contains(accessor.getSessionId())) {
                    String roomId = input.substring(startIndex+7, endIndex);
                    connectedUsers.add(roomId+"_"+accessor.getSessionId());
                    int roomcount = roomId.length();
                    long count = connectedUsers.stream()
                            .filter(s -> s.startsWith(roomId))
                            .count();
                    messagingTemplate.convertAndSend("/topic/"+roomId+"/connectedUsers",count);
                }
            }
        }
    }

//  監聽斷開連線
    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String roomId = connectedUsers.stream()
                .filter(s -> s.contains(sessionId))
                .findFirst()
                .map(s -> s.split("_")[0])
                .orElse(null);
        connectedUsers.removeIf(s -> s.contains(sessionId));
        messagingTemplate.convertAndSend("/topic/"+roomId+"/connectedUsers",-1);
    }

    public int userCount(){
        return connectedUsers.size();
    }

}

