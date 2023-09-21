package com.mozza.springboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBoilerplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoilerplateApplication.class, args);
    }

}
