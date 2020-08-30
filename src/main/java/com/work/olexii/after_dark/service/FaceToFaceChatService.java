package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.*;
import com.work.olexii.after_dark.repos.FaceToFaceChatRepos;
import com.work.olexii.after_dark.repos.FaceToFaceMessageRepos;
import com.work.olexii.after_dark.repos.UserRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class FaceToFaceChatService {
    @Autowired
    private FaceToFaceChatRepos chatRepos;
    @Autowired
    private FaceToFaceMessageRepos messageRepos;
    @Autowired
    private UserRepo userRepo;
    public static Comparator<FaceToFaceMessage> BY_ID;
    public static Comparator<FaceToFaceMessage> BY_CREATE_TIME;

    static {
        BY_ID = new Comparator<FaceToFaceMessage>() {
            @Override
            public int compare(FaceToFaceMessage o1, FaceToFaceMessage o2) {
                return (int) (o1.getId() - o2.getId());
            }
        };

        BY_CREATE_TIME = new Comparator<FaceToFaceMessage>() {
            @Override
            public int compare(FaceToFaceMessage o1, FaceToFaceMessage o2) {
                return (int) (o1.getEpochSecond() - o2.getEpochSecond());
            }
        };
    }


    public List<User> getUserChatters(User user) {
        List<User> userChatters = new ArrayList<>();
        List<FaceToFaceChatEntity> chatEntities = chatRepos.findAll();

        for (FaceToFaceChatEntity chatEntity : chatEntities) {
            if (chatEntity.getUser1().getId() == user.getId()) {
                userChatters.add(chatEntity.getUser2());
            }
            if (chatEntity.getUser2().getId() == user.getId()) {
                userChatters.add(chatEntity.getUser1());
            }

        }
        return userChatters;
    }

    private List<FaceToFaceChatEntity> getUserChats(User user) {
        List<FaceToFaceChatEntity> userChats = new ArrayList<>();
        List<FaceToFaceChatEntity> chatEntities = chatRepos.findAll();

        for (FaceToFaceChatEntity chatEntity : chatEntities) {
            if (chatEntity.getUser1().getId() == user.getId() || chatEntity.getUser2().getId() == user.getId()) {
                userChats.add(chatEntity);
            }

        }
        return userChats;

    }

    public User addChatter(User user1, long id) {
        FaceToFaceChatEntity chatEntity = new FaceToFaceChatEntity();
        User user2 = userRepo.getOne(id);
        chatEntity.setUser1(user1);
        chatEntity.setUser2(user2);
        chatRepos.save(chatEntity);
        return user2;
    }

    public List<User> getOtherUsers(User user) {
        List<User> users = userRepo.findAll();
        List<User> otherUsers = new ArrayList<>();
        List<User> userChatters = getUserChatters(user);

        for (User user1 : users) {
            boolean isChattering = true;
            for (User user2 : userChatters) {
                if (user1.getId() == user2.getId()) {
                    isChattering = false;
                }
            }
            if (isChattering) {
                otherUsers.add(user1);
            }
        }

        return otherUsers;
    }

    public List<FaceToFaceMessage> getPrivateMessages(User user, long id) {
        List<FaceToFaceMessage> privateMessages = new ArrayList<>();
        List<FaceToFaceMessage> allPrivateMessages = messageRepos.findAll();
        User user2 = userRepo.getOne(id);
        for (FaceToFaceMessage privateMessage : allPrivateMessages) {
            if((privateMessage.getFromUser().getId()==user.getId() && privateMessage.getToUser().getId()==user2.getId()) ||
                    (privateMessage.getFromUser().getId()==user2.getId() && privateMessage.getToUser().getId()==user.getId())){
               privateMessages.add(privateMessage);
            }
        }
        for (FaceToFaceMessage message : privateMessages) {
            if (message.getToUser().getId()==user.getId() && !message.isRead()){
                message.setRead(true);
                messageRepos.save(message);
            }
        }

        Collections.sort(privateMessages, BY_CREATE_TIME);


        return privateMessages;
    }

    public boolean sendPrivateMessage(User user, FaceToFaceMessage message, long id) {
        User user2 = userRepo.getOne(id);
        message.setFromUser(user);
        message.setToUser(user2);
        String normalizedMessageText = normalizeMessageText(message.getText());
        message.setText(normalizedMessageText);
        System.out.println(message.getText());
        message.setCreateTime(LocalDateTime.now());
        message.setRead(false);
        LocalDateTime dateTimeUTC = LocalDateTime.now(ZoneOffset.UTC);
        long seconds = dateTimeUTC.toEpochSecond(ZoneOffset.UTC);
        message.setEpochSecond(seconds);
        messageRepos.save(message);
        return true;
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

    public boolean deleteMessage(long id) {

        FaceToFaceMessage message = messageRepos.getOne(id);
        messageRepos.delete(message);
        return true;
    }

    public boolean changeMessage(FaceToFaceMessage message, long msgID, User user, long toUserID) {
        FaceToFaceMessage messageFromDB = messageRepos.getOne(msgID);
        User user2 = userRepo.getOne(toUserID);
        message.setFromUser(user);
        message.setToUser(user2);
        message.setRead(false);
        message.setCreateTime(messageFromDB.getCreateTime());
        message.setEpochSecond(messageFromDB.getEpochSecond());
        BeanUtils.copyProperties(message, messageFromDB, "id");

        return true;
    }

    public List<FaceToFaceMessage> isChatChanged(List<FaceToFaceMessage> messages) {
        List<FaceToFaceMessage> messagesFromDB = messageRepos.findAll();
        Collections.sort(messagesFromDB, BY_ID);
        if (!messages.equals(messagesFromDB)) {
            return messagesFromDB;
        }
        return Collections.EMPTY_LIST;

    }

    public List<User> isThereAreNewMessages(User user) {
        List<User> users = new ArrayList<>();
        List<FaceToFaceMessage> allPrivateMessages = messageRepos.findAll();
        for (FaceToFaceMessage privateMessage : allPrivateMessages) {
            if (privateMessage.getToUser().getId()==user.getId() && !privateMessage.isRead()){
                users.add(privateMessage.getFromUser());
            }
        }

        return users;
    }
}
