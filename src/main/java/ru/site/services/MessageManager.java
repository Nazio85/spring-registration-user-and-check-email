package ru.site.services;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class MessageManager {
    public static final String RU = "ru";
    public static final String EN = "en";


    public String getMessage(String key, String locale){
        if (locale == null) return prepareMessage.RU.getMessage(key);
        switch (locale) {
            case EN:
                return prepareMessage.EN.getMessage(key);
            default:
                return prepareMessage.RU.getMessage(key);
        }
    }

    private enum prepareMessage {
        RU(ResourceBundle.getBundle("messages", new Locale(MessageManager.RU))),
        EN(ResourceBundle.getBundle("messages", new Locale(MessageManager.EN)));

        private ResourceBundle bundle;

        prepareMessage(ResourceBundle bundle) {
            this.bundle = bundle;
        }

        public String getMessage(String key){
            return bundle.getString(key);
        }
    }
}
