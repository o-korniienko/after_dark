package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.Role;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.service.CharacterService;
import com.work.olexii.after_dark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CharacterService characterService;

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "usersEdit";
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping
    public String userSave(@RequestParam("userId") User user,
                           @RequestParam Set<String> form,
                           @RequestParam String username) {
        userService.saveUser(user, username, form);

        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @GetMapping("/profile_update")
    @ResponseBody
    public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String password,
                                @RequestParam String email, @Valid User validUser,
                                BindingResult bindingResult) {

        String error = null;
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            for (String value : errors.values()) {
                if (value.equals("Email is not correct")) {
                    error = value;
                    return error;
                }
            }
        }
        error = userService.updateProfile(user, password, email);

        return error;
    }


}
