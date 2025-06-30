package com.twitter_backend_spring_boot.twitter.api.test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
@Tag(name = "Test APIs", description = "Operations related to tests")
public class TestController {

    @PostMapping
    @Operation(summary = "Create a new test", description = "Publishes a test for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "test created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public String createTest(
            @RequestParam @Schema(description = "test content", example = "Hello Twitter!") String content) {
        return "test posted: " + content;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test by ID")
    public String getTest(@PathVariable @Schema(example = "123") String id) {
        return "test with ID: " + id;
    }
}
