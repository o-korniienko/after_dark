package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.repos.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public Message getChart() {
        Message message = messageRepo.findByTag("charter");
        return message;
    }

    public Message updateChart(Message message, long id) {
        Message messageFromDB = messageRepo.getOne(id);
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return message;
    }

    public Message createMessage(Message message) {
        message.setTag("recruiting");
        messageRepo.save(message);
        return message;
    }
}
