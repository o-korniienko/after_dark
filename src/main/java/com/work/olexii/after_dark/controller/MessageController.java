package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {


    @Autowired
    MessageService messageService;

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageService.getChatMessages();
    }

    @PostMapping("/msg")
    public Message createMessage(@AuthenticationPrincipal User user, @RequestBody Message message) {
        return messageService.createMessage(user, message);
    }

    @GetMapping("/charter")
    public Message getChart() {
        return messageService.getChart();
    }

    @PutMapping("/charter")
    public Message updateChart(@RequestParam(value = "id") long id, @RequestBody Message message) {
        return messageService.updateChart(message, id);
    }

    @GetMapping("/recruiting")
    public Message getRecruitingText() {
        return messageService.getRecruitingText();
    }

    @PutMapping("/recruiting")
    public Message updateRecruitingText(@RequestParam(value = "id") long id, @RequestBody Message message) {
        return messageService.updateRecruitingText(message, id);
    }

    @PostMapping("/send_request")
    public Message sendRequest(@RequestBody Message message) {
        return messageService.sendRequest(message);
    }
}
