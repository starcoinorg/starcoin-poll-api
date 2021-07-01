package org.starcoin.poll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class StarcoinPollApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarcoinPollApiApplication.class, args);
    }

}
