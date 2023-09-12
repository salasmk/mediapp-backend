package com.mitocode.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    public void sendMail(Mail mail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getModel());

        String html = templateEngine.process("email/email-template", context);
        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        helper.addAttachment("MyTestFile.txt", new ByteArrayResource("test".getBytes()));

        emailSender.send(message);
    }

}
