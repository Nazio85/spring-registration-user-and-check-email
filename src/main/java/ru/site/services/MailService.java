package ru.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.site.configuration.MyConfig;

@Controller
@RequestMapping("send")
public class MailService {

    @Autowired
    JavaMailSender mailSender;

//    @RequestMapping()
    public boolean sendConfirmation(String sendTo, String subject, String bodyMail){
        try {
//            MimeMessage mailMessage = mailSender.createMimeMessage();
//            try {
//                MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
//                messageHelper.setFrom("nazio85@ya.ru");
//                messageHelper.setSubject("Registration");
//                messageHelper.setText("Тестовое письмо");
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(sendTo);
        mailMessage.setFrom(MyConfig.LOGIN_FOR_EMAIL);
        mailMessage.setSubject(subject);
        mailMessage.setText(bodyMail);

            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
        }

        return false;
    }

}
