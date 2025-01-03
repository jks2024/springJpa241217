package com.kh.springJpa241217.controller;

import com.kh.springJpa241217.dto.ChatRoomReqDto;
import com.kh.springJpa241217.dto.ChatRoomResDto;
import com.kh.springJpa241217.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    @PostMapping("/new")  // 채팅방 개설
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomReqDto chatRoomReqDto) {
        log.info("chatRoomReqDto : {}", chatRoomReqDto);
        ChatRoomResDto chatRoomResDto = chatService.createRoom(chatRoomReqDto.getName());
        return ResponseEntity.ok(chatRoomResDto.getRoomId());
    }
    @GetMapping("/list")  // 채팅방 개설 목록을 전달
    public List<ChatRoomResDto> findAllRoom() {
        return chatService.findAllRoom();
    }
    @GetMapping("/room/{roomId}")
    public ChatRoomResDto findRoomById(@PathVariable String roomId) {
        return chatService.findRoomById(roomId);
    }

}
