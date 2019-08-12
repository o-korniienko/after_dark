package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {


    @Autowired
    MessageService messageService;

    @PostMapping("/msg")
    public Message createMessage(@RequestBody Message message){
        return messageService.createMessage(message);
    }

    @GetMapping("/charter")
    public Message getChart() {
        return messageService.getChart();
    }

    @PutMapping("/charter")
    public Message updateChart(@RequestParam(value = "id") long id, @RequestBody Message message) {
        return messageService.updateChart(message, id);
    }
}
