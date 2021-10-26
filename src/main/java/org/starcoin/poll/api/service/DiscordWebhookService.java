package org.starcoin.poll.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscordWebhookService implements WebhookService {
    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    @Value("${discord.webhook-url}")
    private String webhookUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void post(String subject, String text) {
        Message message = new Message(subject + "\n" + text);
        post(message);
    }

    private void post(Message message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        //headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        try {
            restTemplate.postForLocation(webhookUrl, new HttpEntity<>(message, headers));
        } catch (RuntimeException exception) {
            logger.error("Post to discord webhook error.", exception);
        }
    }

    public static class Message {
        private String content;

        public Message() {

        }

        public Message(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
