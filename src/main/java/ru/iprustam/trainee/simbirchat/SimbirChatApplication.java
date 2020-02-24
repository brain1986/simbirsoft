package ru.iprustam.trainee.simbirchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SimbirChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimbirChatApplication.class, args);
    }

}
