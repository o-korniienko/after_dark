package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Message;
import com.work.olexii.after_dark.domain.Recipient;
import com.work.olexii.after_dark.domain.RecipientTag;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.repos.MessageRepo;
import com.work.olexii.after_dark.repos.RecipientRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    @Autowired
    private RecipientRepo recipientRepo;

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
        List<Message> messages = messageRepo.findByTag("charter");
        if (messages.size() != 0) {
            Message message = messages.get(0);
            return message;
        }
        return null;
    }

    public Message updateChart(Message message) {
        List<Message> messages = messageRepo.findByTag("charter");
        Message messageFromDB = null;
        message.setTag("charter");
        message.setCreateTime(LocalDateTime.now());
        message.setEpochSecond(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC));
        if (messages.size() != 0) {
            messageFromDB = messages.get(0);
            BeanUtils.copyProperties(message, messageFromDB, "id");
            return message;
        }
        messageRepo.save(message);
        return message;
    }

    public Message createMessage(User user, Message message) {
        message.setUser(user);
        message.setTag("chat_message");
        message.setCreateTime(LocalDateTime.now());
        LocalDateTime dateTimeUTC = LocalDateTime.now(ZoneOffset.UTC);
        long seconds = dateTimeUTC.toEpochSecond(ZoneOffset.UTC);
        message.setEpochSecond(seconds);
        messageRepo.save(message);
        return message;
    }

    public Message getRecruitingText() {
        List<Message> messages = messageRepo.findByTag("recruiting");
        if (messages.size() != 0) {
            Message message = messages.get(0);
            return message;
        }
        return null;
    }

    public Message updateRecruitingText(Message message) {
        List<Message> messages = messageRepo.findByTag("recruiting");
        Message messageFromDB = null;
        message.setTag("recruiting");
        message.setCreateTime(LocalDateTime.now());
        message.setEpochSecond(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC));
        if (messages.size() != 0) {
            messageFromDB = messages.get(0);
            BeanUtils.copyProperties(message, messageFromDB, "id");
            return message;
        }
        messageRepo.save(message);
        return message;
    }

    public Message sendRequestToRecruiting(Message message) {
        List<Recipient> recipients = recipientRepo.findAll();

        for (Recipient recipient : recipients) {
            if (recipient.getTags().contains(RecipientTag.RECRUITING)) {
                mailSender.send(recipient.getEmailAddress(), "Запрос на вступление в гильдию", message.getText());
        }
        }
        return message;
    }

    public Message sendRequestToSupport(Message message) {
        List<Recipient> recipients = recipientRepo.findAll();

        for (Recipient recipient : recipients) {
            if (recipient.getTags().contains(RecipientTag.SUPPORT)) {
                mailSender.send(recipient.getEmailAddress(), "Support request", message.getText());
            }
        }
        return message;
    }

    public List<Message> getChatMessages() {
        List<Message> messages = messageRepo.findByTag("chat_message");
        Collections.sort(messages, BY_ID);
        return messages;
    }


    public Message changeMessage(Message message, long id, User user) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("chat_message");
        message.setUser(user);
        message.setCreateTime(LocalDateTime.now());
        message.setEpochSecond(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC));
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
        if (!messages.equals(allChatMessages)) {
            return allChatMessages;
        }
        return Collections.EMPTY_LIST;
    }

    public List<Message> getAllAnnouncements() {
        List<Message> messages = messageRepo.findByTag("announcement");
        Collections.sort(messages, BY_ID);
        return messages;
    }

    public Message createAnnouncement(User user, Message message) {
        message.setUser(user);
        message.setTag("announcement");
        message.setCreateTime(LocalDateTime.now());
        message.setEpochSecond(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC));
        messageRepo.save(message);

        return message;
    }

    public Message changeAnnouncement(Message message, long id, User user) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("announcement");
        message.setUser(user);
        message.setCreateTime(LocalDateTime.now());
        message.setEpochSecond(LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC));
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return message;
    }

}
