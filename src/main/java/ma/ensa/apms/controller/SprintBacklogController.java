package ma.ensa.apms.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.SprintBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.SprintBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.service.SprintBacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sprint-backlogs")
@RequiredArgsConstructor
public class SprintBacklogController {

    private final SprintBacklogService sprintBacklogService;

    @PostMapping
    public ResponseEntity<SprintBacklogResponse> createSprintBacklog(@RequestBody SprintBacklogRequest request) {
        SprintBacklogResponse response = sprintBacklogService.createSprintBacklog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklogResponse> getSprintBacklogById(@PathVariable UUID id) {
        SprintBacklogResponse response = sprintBacklogService.getSprintBacklogById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SprintBacklogResponse>> getAllSprintBacklogs() {
        List<SprintBacklogResponse> response = sprintBacklogService.getAllSprintBacklogs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintBacklogResponse> updateSprintBacklog(
            @PathVariable UUID id, @RequestBody SprintBacklogRequest request) {
        SprintBacklogResponse response = sprintBacklogService.updateSprintBacklog(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprintBacklog(@PathVariable UUID id) {
        sprintBacklogService.deleteSprintBacklog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/user-stories")
    public ResponseEntity<List<UserStoryResponse>> getUserStoriesBySprintBacklogId(@PathVariable UUID id) {
        List<UserStoryResponse> userStories = sprintBacklogService.getUserStoriesBySprintBacklogId(id);
        return ResponseEntity.ok(userStories);
    }

    @PostMapping("/{id}/user-stories")
    public ResponseEntity<UserStoryResponse> addUserStoryToSprintBacklog(
            @PathVariable UUID id, @RequestBody UserStoryRequest userStoryRequest) {
        UserStoryResponse userStoryResponse = sprintBacklogService.addUserStoryToSprintBacklog(id, userStoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userStoryResponse);
    }

    @DeleteMapping("/{id}/user-stories/{userStoryId}")
    public ResponseEntity<Void> removeUserStoryFromSprintBacklog(
            @PathVariable UUID id, @PathVariable UUID userStoryId) {
        sprintBacklogService.removeUserStoryFromSprintBacklog(id, userStoryId);
        return ResponseEntity.noContent().build();
    }
}