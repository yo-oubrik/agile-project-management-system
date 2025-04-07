package ma.ensa.apms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.UserStoryService;

@RestController
@RequestMapping("/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PostMapping
    public ResponseEntity<UserStoryDTO> create(@Valid @RequestBody UserStoryCreationDTO dto) {
        return new ResponseEntity<>(userStoryService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStoryDTO> getUserStoryById(@PathVariable Long id) {
        return ResponseEntity.ok(userStoryService.getUserStoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> updateUserStory(
            @PathVariable Long id,
            @Valid @RequestBody UserStoryCreationDTO dto) {
        return ResponseEntity.ok(userStoryService.updateUserStory(id, dto));
    }

    @PutMapping("/{id}/link-to-epic/{epicId}")
    public ResponseEntity<UserStoryDTO> linkToEpic(
            @PathVariable Long id,
            @PathVariable Long epicId){
        return ResponseEntity.ok(userStoryService.linkToEpic(id, epicId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserStoryDTO> changeStatus(@PathVariable Long id, @RequestParam UserStoryStatus status) {
        UserStoryDTO updated = userStoryService.changeStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{productBacklogId}/sorted-by-priority")
    public ResponseEntity<List<UserStoryDTO>> getAllSortedByPriority(@PathVariable Long productBacklogId) {
        List<UserStoryDTO> stories = userStoryService.getBacklogSorted(productBacklogId);
        return ResponseEntity.ok(stories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userStoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}