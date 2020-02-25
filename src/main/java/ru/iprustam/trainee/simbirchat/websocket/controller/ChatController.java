package ru.iprustam.trainee.simbirchat.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.iprustam.trainee.simbirchat.repository.ChatMessageRepository;
import ru.iprustam.trainee.simbirchat.repository.ChatRoomRepository;
import ru.iprustam.trainee.simbirchat.repository.ChatUserRepository;

import javax.validation.constraints.NotEmpty;

@Controller
public class ChatController {

    ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatController(ChatUserRepository chatUserRepository, ChatRoomRepository chatRoomRepository,
                          ChatMessageRepository chatMessageRepository
                          ) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @GetMapping("deleteroom")
    public String deleteRoom() {
        this.chatRoomRepository.deleteById(1L);
        return "login";
    }

    @GetMapping("login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("chat")
    public String doLogin(Model model, @NotEmpty String username, Integer usergroup) {
        model.addAttribute("username", username);
        model.addAttribute("usergroup", usergroup);

        return "chat";
    }

}
