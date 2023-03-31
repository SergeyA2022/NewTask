package com.haulmont.testTask;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class TestTaskApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskApplication.class, args);
    }

}
