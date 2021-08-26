package org.starcoin.poll.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    private final String from;

    //private final TemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender, String from) { //, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void templateMailAndSend(String template, String subject, Map<String, Object> params, List<String> to) throws MessagingException {
        if (true) {
            throw new UnsupportedOperationException();
        }
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(this.from);
        String[] receiverArray = new String[to.size()];
        mimeMessageHelper.setTo(to.toArray(receiverArray));
        mimeMessageHelper.setSubject(subject);
//        Context ctx = new Context();
//        ctx.setVariable("emailParam", params);
//        String emailText = this.templateEngine.process(template, ctx);
//        mimeMessageHelper.setText(emailText, true);

        this.mailSender.send(mimeMessage);
    }

    public void sendMail(String subject, String content, List<String> to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.from);
        String[] receiverArray = new String[to.size()];
        message.setTo(to.toArray(receiverArray));
        message.setSubject(subject);
        message.setText(content);
        try {
            this.mailSender.send(message);
        } catch (MailException e) {
            LOG.error("Send mail error.", e);
        }
    }
}
