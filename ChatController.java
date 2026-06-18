package com.ai.doctor.controller;

import com.ai.doctor.beans.ChatEntity;
import com.ai.doctor.service.AIService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/ai")
@Slf4j
public class ChatController {

    @Resource
    private AIService aiService;

    @GetMapping("/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "hello,who are you?") String message) {
        return aiService.aiChat(message);
    }

    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "hello,who are you?") String message) {
        return aiService.aiChatStream(message);
    }

    @GetMapping("/generateStream2")
    public List<String> generateStream2(@RequestParam(value = "message", defaultValue = "hello,who are you?") String message) {
        return aiService.aiChatStream2(message);
    }

    @PostMapping("/chat")
    public String chatWithDoctor(@RequestBody ChatEntity chatEntity) {
        log.info("chatEntity="+chatEntity);
        String currentUserName = chatEntity.getCurrentUserName();
        String message = chatEntity.getMessage();
        return aiService.chatWithDoctor(currentUserName,message);
    }
}