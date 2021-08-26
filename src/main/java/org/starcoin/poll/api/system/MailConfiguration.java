package org.starcoin.poll.api.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mail.javamail.JavaMailSender;
import org.starcoin.poll.api.service.MailService;

import javax.annotation.Resource;

@Conditional({MailConfiguration.MailSenderCondition.class})
@Configuration
public class MailConfiguration {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

//    @Resource
//    private TemplateEngine templateEngine;

    @Bean({"mail"})
    @Conditional({MailSenderCondition.class})
    public MailService mailSystem() {
        return new MailService(this.mailSender, this.from);//, this.templateEngine);
    }

    public static class MailSenderCondition implements Condition {
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            String isEnableMail = conditionContext.getEnvironment().getProperty("spring.mail.enable", "false");
            return Boolean.parseBoolean(isEnableMail);
        }
    }
}
