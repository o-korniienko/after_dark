package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainController {


    @GetMapping("/")
    public String greeting() {
        return "home";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/information")
    public String aboutAD() {
        return "/information";
    }

    @GetMapping("/active")
    @ResponseBody
    public User getActiveUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return null;
        }
        return user;
    }

    @GetMapping("/chat_room")
    public String chat(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute(user);
        return "/chat_room";
    }


}
