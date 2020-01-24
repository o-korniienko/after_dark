package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Recipient;
import com.work.olexii.after_dark.domain.RecipientTag;
import com.work.olexii.after_dark.repos.RecipientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecipientService {

    @Autowired
    private RecipientRepo recipientRepo;


    public List<Recipient> getAllRecipients() {
        return recipientRepo.findAll();
    }

    public List<Recipient> deleteRecipient(Recipient recipient) {
        recipientRepo.delete(recipient);
        return recipientRepo.findAll();
    }

    public Recipient changeRecipient(Recipient recipient, Set<String> tags) {
        recipient.getTags().clear();
        for (String tag : tags) {
            recipient.getTags().add(RecipientTag.valueOf(tag));
        }
        recipientRepo.save(recipient);
        return recipient;
    }

    public List<Recipient> addRecipient(String email) {
        Recipient recipient = new Recipient();
        recipient.setEmailAddress(email);

        recipientRepo.save(recipient);
        return recipientRepo.findAll();

    }
}
