package com.example.booking_hotel.service;

import com.example.booking_hotel.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {


     JavaMailSender mailSender;
     SpringTemplateEngine templateEngine;
    @Async
    public void sendEmail(String to, String subject, Map<String, Object> model, String templateName){
        MimeMessage message = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariables(model);

            String htmlContent = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void senEmailUserWithRegister(User user) {
        if(user != null) {
            Map<String, Object> model = new HashMap<>();
            model.put("user", user.getUsername());
            sendEmail(user.getEmail(), "Chúc mừng! Tài khoản DevJob của bạn đã được đăng kí thành công", model, "register");
        }
    }

}
