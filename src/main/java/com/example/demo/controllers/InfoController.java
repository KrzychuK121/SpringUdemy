package com.example.demo.controllers;

import com.example.demo.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    public InfoController(
        DataSourceProperties dataSource,
        TaskConfigurationProperties configurationProperties
    ) {
        this.dataSource = dataSource;
        this.configurationProperties = configurationProperties;
    }

    private DataSourceProperties dataSource;
    private TaskConfigurationProperties configurationProperties;

    @GetMapping("/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp(){
        return configurationProperties
            .getTaskTempConf()
                .isAllowMultipleTasks();
    }
}
