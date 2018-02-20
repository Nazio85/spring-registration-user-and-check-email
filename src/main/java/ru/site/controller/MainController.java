package ru.site.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/")
public class MainController {

    @RequestMapping
    public String homePage(Model model){
        loadHead(model);
        return "index";
    }

    public static void loadHead(Model model) {
        model.addAttribute("title", "site.ru");
        model.addAttribute("discription", "тестовое описание");
        model.addAttribute("User", getCurrentUser());
    }

    private static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")){
            return null;
        }else
            return (UserDetails) authentication.getPrincipal();
    }

}
