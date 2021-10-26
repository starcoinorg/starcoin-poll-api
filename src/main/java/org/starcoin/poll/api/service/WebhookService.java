package org.starcoin.poll.api.service;

public interface WebhookService {
    void post(String subject, String text);
}
