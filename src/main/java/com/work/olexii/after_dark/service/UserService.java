package com.work.olexii.after_dark.service;

import com.work.olexii.after_dark.domain.Role;
import com.work.olexii.after_dark.domain.User;
import com.work.olexii.after_dark.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(s);
        if (user != null && user.getActivationCode() != null) {
           user = null;
        }
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        if (!StringUtils.isEmpty(user.getEmail())) {
            user.setActivationCode(UUID.randomUUID().toString());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        sendMessage(user);
        userRepo.save(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to After Dark. Please , visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public void saveUser(User user, String username, Set<String> form) {
        user.setUsername(username);
        user.getRoles().clear();

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        for (String s : form) {
            if (roles.contains(s)) {
                user.getRoles().add(Role.valueOf(s));
            }
        }
        userRepo.save(user);
    }

    public String updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (!StringUtils.isEmpty(email) && email != null && !email.equals(userEmail) &&
                userEmail != null);

        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }

        if (isEmailChanged && StringUtils.isEmpty(password)) {
            return "Email is changed";
        }
        if (!StringUtils.isEmpty(password) && !isEmailChanged) {
            return "password is changed";
        }
        if (isEmailChanged && !StringUtils.isEmpty(password)) {
            return "Email and password are changed";
        }
        return "";
    }

    public String[] checkUser(String identification) {
        String[] errors = new String[1];
        User user = userRepo.findByEmail(identification);

        if (user == null) {
            user = userRepo.findByUsername(identification);
            if (user == null) {
                errors[0] = "Пользователь не найден!!!";
                return errors;
            }
        }
        String message =
                "Для восстановления пароля перейдите по ссылке: http://localhost:8080/recover/" + user.getId();
        String subject = "Восстановление пароля";
        mailSender.send(user.getEmail(), subject, message);

        errors[0] = "Вам на почту было отправлено письмо. " +
                "Для восстановления доступа к учетной записи перейдите по ссылке в нем.";

        user.setActive(false);
        userRepo.save(user);
        return errors;
    }

    public User getUser(long id) {
        return userRepo.getOne(id);
    }

    public boolean isPasswordChanged(String password, String password2, long id) {
        boolean isPasswordEmpty = StringUtils.isEmpty(password);
        boolean isConfirmEmpty = StringUtils.isEmpty(password2);
        User user = userRepo.getOne(id);
        if (user == null || isConfirmEmpty || isPasswordEmpty || user.isActive() || !password.equals(password2)) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        userRepo.save(user);
        return true;
    }

    public User getOneUser(long id) {

        return userRepo.getById(id);
    }
}
