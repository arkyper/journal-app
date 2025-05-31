package com.arkyper.journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health-Check APIs", description = "Health-Check APIs")
public class HealthCheckController {
    
    @GetMapping("/health-check")
    @Operation(summary = "Health-Check", description = "Health-Check API")
    public String healthCheck() {
        return "Server is running";
    }
}
