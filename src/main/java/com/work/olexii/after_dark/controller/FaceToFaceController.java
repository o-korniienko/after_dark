package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.FaceToFaceMessage;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.service.FaceToFaceChatService;
import com.work.olexii.after_dark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class FaceToFaceController {

    @Autowired
    private FaceToFaceChatService chatService;
    @Autowired
    private UserService userService;

    @GetMapping("get_chatters")
    public List<User> getUserChatters(@AuthenticationPrincipal User user) {

        return chatService.getUserChatters(user);

    }

    @GetMapping("/get_other_users")
    public List<User> getUsers(@AuthenticationPrincipal User user) {
        return chatService.getOtherUsers(user);
    }

    @PostMapping("add_chatter")
    public boolean addChatter(@AuthenticationPrincipal User user1, @RequestParam(value = "id") long id) {
        chatService.addChatter(user1, id);
        return true;
    }

    @GetMapping("get_private_messages")
    public List<FaceToFaceMessage> getPrivateMessages(@AuthenticationPrincipal User user, @RequestParam(value = "id") long id) {

        return chatService.getPrivateMessages(user, id);

    }

    @PostMapping("/send_private_message/{id}")
    public boolean sentPrivateMessage(@AuthenticationPrincipal User user, @RequestBody FaceToFaceMessage message,@PathVariable("id") long id) {

        return chatService.sendPrivateMessage(user, message, id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete_private_message")
    public boolean deleteMessage(@RequestParam(value = "id") long id) {
        return chatService.deleteMessage(id);
    }

    @PutMapping("/change_private_message")
    public boolean changeMessage(@AuthenticationPrincipal User user, @RequestParam(value = "msg_id") long msgID,
                                 @RequestBody FaceToFaceMessage message, @RequestParam(value = "user2_id") long toUserID) {
        return chatService.changeMessage(message, msgID, user, toUserID);
    }

    @PostMapping("/is_private_chat_changed")
    public List<FaceToFaceMessage> isChatChanged(@RequestBody List<FaceToFaceMessage> messages) {
        List<FaceToFaceMessage> messages1 = chatService.isChatChanged(messages);
        return messages1;
    }

    @GetMapping("is_new_messages")
    public List<User> isThereAreNewMessages(@AuthenticationPrincipal User user){
        List<User> users = chatService.isThereAreNewMessages(user);
        if (!users.isEmpty()){
            return users;
        }
        return Collections.emptyList();
    }



}
