package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task.template")
public class TaskTemplateConfiguration {
    private boolean allowMultipleTasks;

    public boolean isAllowMultipleTasks() {
        return allowMultipleTasks;
    }

    public void setAllowMultipleTasks(boolean allowMultipleTasks) {
        this.allowMultipleTasks = allowMultipleTasks;
    }
}
