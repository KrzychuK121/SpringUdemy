package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
    @NestedConfigurationProperty()
    private TaskTemplateConfiguration taskTempConf;

    public TaskConfigurationProperties(TaskTemplateConfiguration taskTempConf) {
        this.taskTempConf = taskTempConf;
    }

    public TaskTemplateConfiguration getTaskTempConf() {
        return taskTempConf;
    }

    public void setTaskTempConf(TaskTemplateConfiguration taskTempConf) {
        this.taskTempConf = taskTempConf;
    }
}
