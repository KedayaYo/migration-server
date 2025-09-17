package com.skysys.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class MigrationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigrationServerApplication.class, args);
    }

}
