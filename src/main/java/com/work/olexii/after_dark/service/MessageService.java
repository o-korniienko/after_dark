package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.repos.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MailSender mailSender;

    public static Comparator<Message> BY_ID;

    static {
        BY_ID = new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o1.getId() - o2.getId());
            }
        };
    }

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
        Collections.sort(messages, BY_ID);
        return messages;
    }


    public Message changeMessage(Message message, long id, User user) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("chat_message");
        message.setUser(user);
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return message;

    }

    public List<Message> deleteMessage(long id) {
        Message message = messageRepo.getOne(id);
        messageRepo.delete(message);

        return messageRepo.findAll();
    }

    public List<Message> isChatChanged(List<Message> messages) {
        List<Message> messagesFromDB = messageRepo.findAll();
        List<Message> allChatMessages = new ArrayList<>();
        for (Message message : messagesFromDB) {
            if (message.getTag().equals("chat_message")) {
                allChatMessages.add(message);
            }
        }
        Collections.sort(allChatMessages, BY_ID);
        if (!messages.equals(allChatMessages)){
            return allChatMessages;
        }
        return Collections.EMPTY_LIST;
    }
}
