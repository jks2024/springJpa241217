package com.kh.springJpa241217.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.springJpa241217.dto.ChatMessageDto;
import com.kh.springJpa241217.dto.ChatRoomResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;  // JSON 문자열로 변환하기 위한 객체
    private Map<String, ChatRoomResDto> chatRooms; // 채팅방 정보를 담을 맵

    @PostConstruct  // 의존성 주입 이후 초기화를 수행하는 메서드
    private void init() {
        chatRooms = new LinkedHashMap<>(); // 채팅방 정보를 담을 맵
    }
    public List<ChatRoomResDto> findAllRoom() { // 채팅방 리스트 반환
        return new ArrayList<>(chatRooms.values());
    }
    public ChatRoomResDto findRoomById(String roomId) {  // 방번호로 채팅방 반환
        return chatRooms.get(roomId);
    }
    // 방 개설하기
    public ChatRoomResDto createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        log.info("UUID : {}", randomId);
        ChatRoomResDto chatRoom = ChatRoomResDto.builder()
                .roomId(randomId)
                .name(name)
                .regDate(LocalDateTime.now())
                .build();
        chatRooms.put(randomId, chatRoom); // 방 생성, 키와 방 정보 추가
         return chatRoom;
    }
    // 방 삭제
    public void removeRoom(String roomId) {
        ChatRoomResDto room = chatRooms.get(roomId);
        if (room != null) {
            if(room.isSessionEmpty()) {
                chatRooms.remove(roomId);
            }
        }
    }
    // 채팅방에 입장한 세션 추가
    public void addSessionAndHandleEnter(String roomId, WebSocketSession session,
                                         ChatMessageDto chatMessage) {
        ChatRoomResDto room = findRoomById(roomId);
        if (room != null) {
            room.getSessions().add(session); // 채팅방에 입장한 세션을 추가
            if(chatMessage.getSender() != null) {  // 채팅방에 입장한 사용자가 있으면
                chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
                // 채팅방에 입장 메시지 전송 코드 추가
                sendMessageToAll(roomId, chatMessage);
            }
            log.debug("새로운 세션 추가 !!!!");
        }
    }
    // 채팅방에서 퇴장한 세션 제거
    public void removeSessionAndHandleExit(String roomId, WebSocketSession session,
                                           ChatMessageDto chatMessage) {
        ChatRoomResDto room = findRoomById(roomId);
        if(room != null) {
            room.getSessions().remove(session); // 채팅방에서 퇴장한 세션 제거
            if(chatMessage.getSender() != null) {  // 채팅방에서 퇴장한 사용자가 있으면
                chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장 하였습니다.");
                // 채팅방에 퇴장 메시지 전송 코드 추가
                sendMessageToAll(roomId, chatMessage);
            }
            log.debug("세션 제거 됨 : {}", session);
            if(room.isSessionEmpty()) {
                removeRoom(roomId); // 세션이 남아 있지 않으면 방삭제
            }
        }
    }
    public void sendMessageToAll(String roomId, ChatMessageDto message) {
        ChatRoomResDto room = findRoomById(roomId);
        if(room != null) {
            for(WebSocketSession session : room.getSessions()) {
                // 해당 세션에 메시지 발송
                sendMessage(session, message);
            }
        }
    }
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("메시지 전송 실패 : {}", e.getMessage());
        }
    }

}
