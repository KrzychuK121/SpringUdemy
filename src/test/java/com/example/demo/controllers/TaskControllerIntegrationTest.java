package com.example.demo.controllers;

import com.example.demo.models.Task;
import com.example.demo.models.TasksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TasksRepository repo;

    @Test
    void httpGet_returnsAllDoneTasks() throws Exception {
        // given
        boolean status = true;

        boolean[] done = {
            false,
            true,
            false,
            true
        };

        Task[] tasks = new Task[]{
            new Task("foo1", LocalDateTime.now()),
            new Task("foo3", LocalDateTime.now()),
            new Task("foo4", LocalDateTime.now()),
            new Task("foo5", LocalDateTime.now()),
        };

        int beforeSaveCount = repo.findByDone(status).size();
        for(int i = 0; i < tasks.length; i++){
            tasks[i].setDone(done[i]);
            repo.save(tasks[i]);
        }
        List<Task> result = repo.findByDone(status);
        int afterSaveCount = result.size();

        // when + then
        //assertEquals(beforeSaveCount + 2, afterSaveCount, "Not enough tasks with done = " + status);
        mockMvc.perform(get("/tasks?isDone=" + status))
        .andDo(print())
        .andExpect(status().is2xxSuccessful());

    }

    @Test
    void httpPost_returnsPostedTask() throws Exception{
        // given
        ObjectMapper mapper = new ObjectMapper();
        Boolean done = true;
        String description = "foo";
        LocalDateTime deadline = LocalDateTime.now();
        Task toSave = new Task(description, deadline);
        toSave.setDone(done);

        Map<String, Object> body = new HashMap<>(3);
        body.put("done", done);
        body.put("description", description);
        body.put("deadline", deadline.toString());

        // when + then
        mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(body))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(
                containsString(done.toString())
            )
        )
        .andExpect(content().string(
                containsString(description)
            )
        );

    }

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        // given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        // when + then
        mockMvc.perform(get("/tasks/" + id))
        .andExpect(status().is2xxSuccessful());
    }
}
