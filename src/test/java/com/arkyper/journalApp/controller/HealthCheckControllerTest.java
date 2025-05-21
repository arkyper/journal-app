package com.arkyper.journalApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
@WebMvcTest(HealthCheckController.class)
public class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test case to check if the health check endpoint returns "Server is running" and HTTP status 200 OK
    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/health-check"))
               .andExpect(status().isOk())
               .andExpect(content().string("Server is running"))
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)); // Optional: Add content type check
    }

    // Test case to specifically check for HTTP status 200 OK
    @Test
    public void testHealthCheckStatus() throws Exception {
        mockMvc.perform(get("/health-check")).andExpect(status().isOk());
    }
}