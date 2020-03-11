package ru.iprustam.trainee.simbirchat.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("username",
                ((UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal())
                        .getUsername());
        return "chat";
    }

}
