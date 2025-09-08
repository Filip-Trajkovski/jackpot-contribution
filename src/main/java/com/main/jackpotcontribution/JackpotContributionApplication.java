package com.main.jackpotcontribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.main.jackpotcontribution")
@SpringBootApplication(scanBasePackages = "com.main.jackpotcontribution")
public class JackpotContributionApplication {

    public static void main(String[] args) {
        SpringApplication.run(JackpotContributionApplication.class, args);
    }

}
