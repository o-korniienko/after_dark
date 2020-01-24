package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.Recipient;
import com.work.olexii.after_dark.service.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/recipient")
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    @GetMapping
    public List<Recipient> getAllRecipients(){
        return recipientService.getAllRecipients();
    }

    @PutMapping
    public Recipient changeRecipient(@RequestParam("recipient_id") Recipient recipient,
                                     @RequestParam Set<String> tags){
        return recipientService.changeRecipient(recipient, tags);
    }

    @PostMapping
    public List<Recipient> addRecipient(@RequestParam String email){
        return recipientService.addRecipient(email);
    }

    @DeleteMapping("{recipient}")
    public List<Recipient> deleteRecipient(@PathVariable Recipient recipient){
        return recipientService.deleteRecipient(recipient);
    }
}
