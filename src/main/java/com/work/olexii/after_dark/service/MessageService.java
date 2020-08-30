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
import java.util.*;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private RecipientRepo recipientRepo;

    public static Comparator<Message> BY_ID;
    private static Comparator<Message> BY_CREATE_TIME;

    static {
        BY_ID = new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o1.getId() - o2.getId());
            }
        };

        BY_CREATE_TIME = new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o1.getEpochSecond() - o2.getEpochSecond());
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
        String normalizedMessageText = normalizeMessageText(message.getText());
        message.setText(normalizedMessageText);
        message.setTag("chat_message");
        System.out.println(message.getText());
        message.setCreateTime(LocalDateTime.now());
        LocalDateTime dateTimeUTC = LocalDateTime.now(ZoneOffset.UTC);
        long seconds = dateTimeUTC.toEpochSecond(ZoneOffset.UTC);
        message.setEpochSecond(seconds);
        messageRepo.save(message);
        return message;
    }

    private String normalizeMessageText(String text) {
        String result = "";
        String[] textArray = text.split("\n");
        for (String textA : textArray) {
            if (textA.length() <= 50) {
                result = result.concat(textA);
            } else {
                String result2 = "";
                char[] litterArray = textA.toCharArray();
                int count = litterArray.length / 50;
                System.out.println(count);
                int[] moves = new int[count];
                int ind = 0;

                List<Integer> gapIndexes = new ArrayList<>();

                for (int i = 0; i < litterArray.length; i++) {
                    if (litterArray[i] == ' ') {
                        gapIndexes.add(i);
                    }
                }

                System.out.println("gap indexes: " + gapIndexes);
                for (int i = 0; i < count; i++) {
                    List<Integer> temporaryIndexes = new ArrayList<>();
                    for (Integer gapIndex : gapIndexes) {
                        if (gapIndex < (50 * (ind + 1)) && gapIndex > (50 * ind)) {
                            temporaryIndexes.add(gapIndex);
                        }

                    }
                    System.out.println("temporary indexes: " + temporaryIndexes.size() + " " + temporaryIndexes);
                    if (temporaryIndexes.size() <= 0) {
                        moves[i] = 50 * (ind + 1);
                    } else {
                        moves[i] = getMaxIndex(temporaryIndexes);
                    }
                    System.out.println("ind: " + ind);
                    ind++;
                }

                System.out.println(Arrays.toString(moves));

                ind = 0;
                for (int i = 0; i < litterArray.length; i++) {
                    if (ind < moves.length && i == moves[ind]) {
                        System.out.println("here");
                        result2 = result2 + "\n";
                        ind++;
                    } else {
                        result2 = result2.concat(String.valueOf(litterArray[i]));
                    }
                }

                result = result + result2;
            }
        }

        return result;
    }

    private int getMaxIndex(List<Integer> temporaryIndexes) {
        int max = 0;
        for (Integer temporaryIndex : temporaryIndexes) {
            if (temporaryIndex > max) {
                max = temporaryIndex;
            }
        }
        return max;
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
        Collections.sort(messages, BY_CREATE_TIME);
        return messages;
    }


    public Message changeMessage(Message message, long id, User user) {
        Message messageFromDB = messageRepo.getOne(id);
        message.setTag("chat_message");
        message.setUser(user);
        message.setCreateTime(messageFromDB.getCreateTime());
        message.setEpochSecond(messageFromDB.getEpochSecond());
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
