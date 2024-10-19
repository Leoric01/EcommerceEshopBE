package com.leoric.ecommerceshopbe.security.auth.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${mailing.frontend.activation-url}")
    String activationUrl;

    public void sendVerificationEmail(String to,
                                      String username,
                                      String subject,
                                      String activationCode) {
        String templateName = EmailTemplateName.ACTIVATE_ACCOUNT.getName();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    "UTF-8"
            );

            Map<String, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("confirmationUrl", activationUrl);
            model.put("activation_code", activationCode);

            Context context = new Context();
            context.setVariables(model);

            helper.setFrom("support@leoric.com");
            helper.setTo(to);
            helper.setSubject(subject + ": " + activationCode);
            String template = templateEngine.process(templateName, context);
            helper.setText(template, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new MailSendException("failed to send email", e);
        }
    }
}
