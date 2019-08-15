package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.repos.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MailSender mailSender;

    public Message getChart() {
        Message message = messageRepo.findByTag("charter");
        return message;
    }

    public Message updateChart(Message message, long id) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("charter");
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return message;
    }

    public Message createMessage(User user, Message message) {
        message.setUser(user);
        message.setTag("chat_message");
        messageRepo.save(message);
        return message;
    }

    public Message getRecruitingText() {
        return messageRepo.findByTag("recruiting");
    }

    public Message updateRecruitingText(Message message, long id) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("recruiting");
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return message;
    }

    public Message sendRequest(Message message) {
        mailSender.send("alyosha21@ukr.net", "Запрос на вступление в гильдию", message.getText());
        return message;
    }

    public List<Message> getChatMessages() {
        Iterable<Message> messageIterable = messageRepo.findAll();
        List<Message> messages = new ArrayList<>();
        for (Message message : messageIterable) {
            if (message.getTag().equals("chat_message")) {
                messages.add(message);
            }
        }
        return messages;
    }
}
