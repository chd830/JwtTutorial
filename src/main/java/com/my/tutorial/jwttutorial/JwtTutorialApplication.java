package com.my.tutorial.jwttutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class JwtTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtTutorialApplication.class, args);
    }

}
