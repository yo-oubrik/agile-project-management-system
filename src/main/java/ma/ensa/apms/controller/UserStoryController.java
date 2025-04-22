package ma.ensa.apms.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.UserStoryService;

@RestController
@RequestMapping("/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PostMapping
    public ResponseEntity<UserStoryResponse> create(@Valid @RequestBody UserStoryRequest dto) {
        return new ResponseEntity<>(userStoryService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStoryResponse> getUserStoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(userStoryService.getUserStoryById(id));
    }

    @GetMapping("/{id}/acceptance-criterias")
    public ResponseEntity<List<AcceptanceCriteriaResponse>> getAcceptanceCriteriasByUserStoryId(@PathVariable UUID id) {
        return ResponseEntity.ok(userStoryService.getAcceptanceCriteriasByUserStoryId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryResponse> updateUserStory(
            @PathVariable UUID id,
            @Valid @RequestBody UserStoryRequest dto) {
        return ResponseEntity.ok(userStoryService.updateUserStory(id, dto));
    }

    @PutMapping("/{id}/link-to-epic/{epicId}")
    public ResponseEntity<UserStoryResponse> linkToEpic(
            @PathVariable UUID id,
            @PathVariable UUID epicId) {
        return ResponseEntity.ok(userStoryService.linkToEpic(id, epicId));
    }

    @PutMapping("/{id}/move-to-sprint/{sprintId}")
    public ResponseEntity<UserStoryResponse> moveToSprint(
            @PathVariable UUID id,
            @PathVariable UUID sprintId) {
        return ResponseEntity.ok(userStoryService.moveToSprint(id, sprintId));
    }

    @PatchMapping("/{id}/change-status")
    public ResponseEntity<UserStoryResponse> changeStatus(@PathVariable UUID id, @Valid @RequestBody UserStoryStatus status) {
        UserStoryResponse updated = userStoryService.changeStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/productBacklog={productBacklogId}&status={status}")
    public ResponseEntity<List<UserStoryResponse>> getUserStoriesByStatus(
            @PathVariable UUID productBacklogId ,
            @PathVariable UserStoryStatus status
    ){
        List<UserStoryResponse> userStories = userStoryService.getUserStoriesByStatusAndProductBacklogId(status,productBacklogId);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/epic/{epicId}")
    public ResponseEntity<List<UserStoryResponse>> getUserStoriesByEpicId(@PathVariable UUID epicId) {
        List<UserStoryResponse> userStories = userStoryService.getUserStoriesByEpicId(epicId);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/sprint-backlog/{sprintId}")
    public ResponseEntity<List<UserStoryResponse>> getUserStoriesBySprintBacklogId(@PathVariable UUID sprintId) {
        List<UserStoryResponse> userStories = userStoryService.getUserStoriesBySprintBacklogId(sprintId);
        return ResponseEntity.ok(userStories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userStoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}