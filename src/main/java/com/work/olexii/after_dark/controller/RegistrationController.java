package com.work.olexii.after_dark.controller;

import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.domain.dto.CaptchaResponseDto;
import com.work.olexii.after_dark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;


@Controller
public class RegistrationController {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;
    @Value("${recaptcha.secret}")
    private String secret;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    @ResponseBody
    public String[] reg(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult) {


        String[] models = new String[8];
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(),
                CaptchaResponseDto.class);
        if (!response.isSuccess()){
            models[4] = "Fill captcha";
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            models[3] = "Password confirmation cannot be empty";
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            models[1] = "Passwords are different";
        }
        if (bindingResult.hasErrors() || !response.isSuccess() || isConfirmEmpty) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            int mds = 5;
            for (String error : errors.values()) {
                models[mds] = error;
                mds++;
            }
            models[0] = "/registration";
            return models;
        }
        if (!userService.addUser(user)) {
            models[2] = "User already Exists!";
            models[0] = "/registration";
            return models;
        }
        models[0] = "/login";
        return models;
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "/login";
    }

}
