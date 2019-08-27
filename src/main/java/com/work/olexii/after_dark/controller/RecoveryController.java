package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecoveryController {

    @Autowired
    private UserService userService;

    @GetMapping("/recovery")
    public String recovery() {
        return "/recovery";
    }

    @GetMapping("/recover/{id}")
    public String recoveryPassword(@PathVariable("id") User user, Model model) {
        model.addAttribute("user_id", user.getId());
        return "/recover";
    }

    @PostMapping("/recovery")
    @ResponseBody
    public String[] checkingUser(@RequestParam(value = "identification") String identification) {
        return userService.checkUser(identification);
    }

    @GetMapping("/recover/request")
    public String changePassword(@RequestParam(value = "password") String password,
                                 @RequestParam(value = "password2") String password2,
                                 @RequestParam(value = "id") long id,
                                 Model model) {
        String[] errors = new String[3];
        boolean isPasswordEmpty = StringUtils.isEmpty(password);
        boolean isConfirmEmpty = StringUtils.isEmpty(password2);
        if (isPasswordEmpty) {
            errors[0] = "Password cannot be empty";
        }
        if (isConfirmEmpty) {
            errors[1] = "Password confirmation cannot be empty";
        }
        if (!isPasswordEmpty && !isConfirmEmpty && !password.equals(password2)) {
            errors[2] = "Passwords are different";
        }

        if (!userService.isPasswordChanged(password, password2, id)) {
            model.addAttribute("errors", errors);
            model.addAttribute("user_id", id);
            return "/recover";
        }

        return "/login";
    }
}
