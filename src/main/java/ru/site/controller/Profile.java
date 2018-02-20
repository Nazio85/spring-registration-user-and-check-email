package ru.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/profile")
public class Profile {


    @RequestMapping(method = RequestMethod.GET)
    public String profile(Model model) {
        return "profile";
    }






}
