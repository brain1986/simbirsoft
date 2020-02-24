package ru.iprustam.trainee.simbirchat.websocket.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.NotEmpty;

@Controller
public class ChatController {

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
