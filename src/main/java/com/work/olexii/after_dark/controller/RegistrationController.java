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

    @GetMapping("/registration/request")
    public String reg(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult, Model model) {


        String[] allErrors = new String[8];
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(),
                CaptchaResponseDto.class);
        if (!response.isSuccess()) {
            allErrors[4] = "Fill captcha";
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        boolean isPasswordsDifferent = false;
        System.out.println(isConfirmEmpty);
        if (isConfirmEmpty) {
            allErrors[3] = "Password confirmation cannot be empty";
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            allErrors[1] = "Passwords are different";
            isPasswordsDifferent = true;
        }
        if (bindingResult.hasErrors() || !response.isSuccess() || isConfirmEmpty || isPasswordsDifferent) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            int mds = 5;

            for (String error : errors.values()) {
                allErrors[mds] = error;
                mds++;
            }
            allErrors[0] = "/registration";
            model.addAttribute("errors", allErrors);
            return "/registration";
        }
        if (!userService.addUser(user)) {
            allErrors[2] = "User already Exists!";
            allErrors[0] = "/registration";
            model.addAttribute("errors", allErrors);
            return "/registration";
        }
        allErrors[0] = "/login";
        model.addAttribute("errors", allErrors);
        return "/login";
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
