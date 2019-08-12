package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.repos.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        Message message = messageRepo.findByTag("recruiting");
        return message;
    }

    public Message updateRecruitingText(Message message, long id) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("recruiting");
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return message;
    }

    public void doIntroRequest(String text) {
        sendMessage(text);
    }

    private void sendMessage(String text) {
        mailSender.send("alyosha21@ukr.net", "Запрос на вступление в гильдию", text);
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Iterable<Message> messageIterable = messageRepo.findAll();
        for (Message message : messageIterable) {
            if (!message.getTag().equals("charter") && !message.getTag().equals("recruiting")) {
                messages.add(message);
            }
        }
        return messages;
    }
}
