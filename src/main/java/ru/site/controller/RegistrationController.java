package ru.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.site.dao.user.RegistrationCodeRepository;
import ru.site.dao.user.RoleRepository;
import ru.site.dao.user.UsersRepository;
import ru.site.model.RegistrationCode;
import ru.site.model.Role;
import ru.site.model.Users;
import ru.site.services.MailService;
import ru.site.services.MessageManager;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class RegistrationController {
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String MESSAGE_FROM_USER = "messageFromUser";
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RegistrationCodeRepository codeRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MailService mailService;
    @Autowired
    MessageManager message;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/login")
    public String logout() {
        return "redirect:/";
    }

    @RequestMapping(value = "/login/success")
    public String loginSuccess() {
        return "redirect:/profile";
    }

    @RequestMapping(value = "/login/registration-code/{code}")
    public String checkCode(@PathVariable String code) {
        RegistrationCode registrationCode = codeRepository.findByUuid(code);
        if (registrationCode != null) {
            if (registrationCode.isNotActivation()) {
                Users user = usersRepository.findByRegistrationCode(registrationCode);
                System.out.println(user.getUsername());

                user.setEnabled(true);
                usersRepository.save(user);
                registrationCode.setNotActivation(false);
                codeRepository.save(registrationCode);
                return "redirect:/login/emailValid";
            } else return "redirect:/login/codeNotEnable";
        } else return "redirect:/login/codeNotEnable";
    }

    @RequestMapping(value = "/login/registration", method = RequestMethod.POST)
    public String registration(@RequestParam String userName,
                               @RequestParam String email, @RequestParam String password) {
//        Создаем роли
        Role role = roleRepository.findByRole(USER);
        if (role == null) {
            role = new Role(ADMIN);
            roleRepository.save(role);
            role = new Role(USER);
            roleRepository.save(role);
        }
        Set<Role> setRoles = new HashSet<>();
        setRoles.add(role);

//        Ищем пользовавтеля по имени и емайлу
        Users searchUserForName = usersRepository.findByUsername(userName);
        Users searchUserForEmail = usersRepository.findByEmail(email);

        if (searchUserForName == null && searchUserForEmail == null) {
            RegistrationCode code = new RegistrationCode();
            codeRepository.save(code);
            Users user = new Users(setRoles, userName, email,
                    passwordEncoder.encode(password), code);
            usersRepository.save(user);

            new Thread(()->{
                mailService.sendConfirmation(email, message.getMessage("mailSubject", MessageManager.RU),
                        message.getMessage("mailBodyText", MessageManager.RU) + code.getUuid());
            }).start();
            return "redirect:/login/registration-ok";
        } else if (searchUserForName != null) {
            return "redirect:/login/registration-fail-name-is-exist";
        } else {
            return "redirect:/login/registration-fail-email-is-exist";
        }
    }

    @RequestMapping(value = "/login/remind-password", method = RequestMethod.POST)
    public String remindPassword(@RequestParam String email) {
        Users user = usersRepository.findByEmail(email);
        if (user != null) {
            RegistrationCode registrationCode = user.getRegistrationCode();

            registrationCode.remindPassword();
            codeRepository.save(registrationCode);

            new Thread(()->{
                mailService.sendConfirmation(email,
                        message.getMessage("mailConfirmPasswordSubject", MessageManager.RU),
                        message.getMessage("remindPasswordBody", MessageManager.RU) + registrationCode.getUuid());
            }).start();

            new Thread(() -> {
                try {
                    Thread.sleep(600000);
                    registrationCode.setUpdatePassword(false);
                    codeRepository.save(registrationCode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else return "redirect:/login/fail";
        return "redirect:/login/remind-password-ok";
    }

    @RequestMapping(value = "/login/remind-password/{code}")
    public String replacePassword(@PathVariable String code, Model model) {
        RegistrationCode registrationCode = codeRepository.findByUuid(code);
        if (registrationCode != null) {
            if (registrationCode.isUpdatePassword()) {
                Users user = usersRepository.findByRegistrationCode(registrationCode);
                model.addAttribute("UniqueCode", code);
                model.addAttribute("user", user);
                return "emailReplace";
            } else return "redirect:/login/codeNotEnableLinkOld";
        } else return "redirect:/login/codeNotEnable";
    }

    @RequestMapping(value = "/login/remind-password/check", method = RequestMethod.POST)
    public String updatePassword(@RequestParam String password, @RequestParam String uniqueCode,
                                 @RequestParam String login) {
        Users user = usersRepository.findByUsername(login);
        if (user != null) {
            RegistrationCode registrationCode = user.getRegistrationCode();
            if (registrationCode != null) {
                if (registrationCode.isUpdatePassword()) {
                    if (registrationCode.getUuid().equals(uniqueCode)) {
                        user.setPassword(bCryptPasswordEncoder.encode(password));
                        usersRepository.save(user);
                        registrationCode.setUpdatePassword(false);
                        registrationCode.setNotActivation(false);
                        codeRepository.save(registrationCode);
                    } else return "redirect:/login/codeNotEnable";
                } else return "redirect:/login/codeNotEnableLinkOld";
            } else return "redirect:/login/codeNotEnable";
        }


        return "redirect:/login/remind-password/success";
    }

    @RequestMapping("/login/codeNotEnableLinkOld")
    public String codeNotEnableLinkOld(Model model) {
        model.addAttribute(MESSAGE_FROM_USER,
                message.getMessage("codeNotEnableLinkOld", MessageManager.RU));
        return "index";
    }

    @RequestMapping("/login/codeNotEnable")
    public String codeNotEnable(Model model) {
        model.addAttribute(MESSAGE_FROM_USER,
                message.getMessage("codeNotEnable", MessageManager.RU));
        return "index";
    }

    @RequestMapping("/login/remind-password/success")
    public String remindPasswordSuccess(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("remindPasswordSuccess", MessageManager.RU));
        return "index";
    }

    @RequestMapping("/login/emailValid")
    public String emailValid(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("emailValid", MessageManager.RU));
        return "index";
    }

    @RequestMapping(value = "/login/registration-ok")
    public String registrationOk(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("checkEmail", MessageManager.RU));
        return "index";
    }

    @RequestMapping(value = "/login/registration-fail-name-is-exist")
    public String registrationFailNameIsExist(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("nameIsExist", MessageManager.RU));
        return "index";
    }

    @RequestMapping(value = "/login/registration-fail-email-is-exist")
    public String registrationFailEmailIsExist(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("emailIsExist", MessageManager.RU));
        return "index";
    }

    @RequestMapping(value = "/login/fail")
    public String loginFail(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("failLogin", MessageManager.RU));
        return "index";
    }

    @RequestMapping(value = "/login/remind-password-ok")
    public String remindPasswordMessage(Model model) {
        model.addAttribute(MESSAGE_FROM_USER, message.getMessage("checkEmail", MessageManager.RU));
        return "index";
    }
}
