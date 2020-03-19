package ru.iprustam.trainee.simbirchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;

@Controller
public class ChatController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("user_id", (UserUtils.getCurrentPrincipal()).getUserId());
        model.addAttribute("username", (UserUtils.getCurrentPrincipal()).getUsername());
        return "chat";
    }

}
