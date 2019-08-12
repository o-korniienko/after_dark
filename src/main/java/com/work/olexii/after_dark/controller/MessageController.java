package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {


    @Autowired
    MessageService messageService;

    @PostMapping("/msg")
    public Message createMessage(@AuthenticationPrincipal User user, @RequestBody Message message){
        return messageService.createMessage(user,message);
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/charter")
    public Message getChart() {
        return messageService.getChart();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/charter")
    public Message updateChart(@RequestParam(value = "id") long id, @RequestBody Message message) {
        return messageService.updateChart(message, id);
    }

    @GetMapping("/recruiting")
    public Message getRecruitingText(){
        return messageService.getRecruitingText();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/recruiting")
    public Message updateRecruitingText(@RequestParam(value = "id") long id, @RequestBody Message message){
        return messageService.updateRecruitingText(message, id);
    }

    @PostMapping("/intro_request")
    public Message introRequest(@RequestBody Message message){
        messageService.doIntroRequest(message.getText());
        return message;
    }
}
