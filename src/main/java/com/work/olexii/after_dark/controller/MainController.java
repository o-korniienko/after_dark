package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.RecipientTag;
import com.work.olexii.after_dark.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

;import javax.jws.soap.SOAPBinding;


@Controller
public class MainController {

    @GetMapping("/")
    public String greeting() {
        return "home";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/information")
    public String aboutAD() {
        return "/information";
    }

    @GetMapping("/active")
    @ResponseBody
    public User getActiveUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return null;
        }
        return user;
    }

    @GetMapping("/chat_room")
    public String chat(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute(user);
        return "/chat_room";
    }


    @GetMapping("/add_update_characters")
    public String charactersAdminPage() {
        return "/add_update_characters";
    }

    @GetMapping("/support")
    public String goToSupport() {
        return "/support";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String goToAdminPage() {
        return "/admin_page";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list_of_recipients")
    public String goToListOfRecipients(Model model) {
        model.addAttribute("tags", RecipientTag.values());
        return "/list_of_recipients";
    }

    @GetMapping("/info/{inf}")
    public String test(@PathVariable("inf") String inf, Model model) {

        model.addAttribute("ind", inf);
        return "/info";
    }

    @GetMapping("/add_chatter_page")
    public String addInterlocutorPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);
        return "/add_chatter";

    }
}