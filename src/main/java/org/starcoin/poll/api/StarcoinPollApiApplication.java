package org.starcoin.poll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableScheduling
public class StarcoinPollApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarcoinPollApiApplication.class, args);
    }

}
