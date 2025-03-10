package ma.ensa.apms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.service.UserStoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PostMapping
    public ResponseEntity<UserStoryDTO> createUserStory(@Valid @RequestBody UserStoryDTO userStoryDTO) {
        UserStoryDTO createdUserStory = userStoryService.save(userStoryDTO);
        return new ResponseEntity<>(createdUserStory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStoryDTO> getUserStoryById(@PathVariable Long id) {
        UserStoryDTO userStory = userStoryService.findById(id);
        return ResponseEntity.ok(userStory);
    }

    @GetMapping
    public ResponseEntity<List<UserStoryDTO>> getAllUserStories() {
        List<UserStoryDTO> userStories = userStoryService.findAll();
        return ResponseEntity.ok(userStories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> updateUserStory(
            @PathVariable Long id,
            @Valid @RequestBody UserStoryDTO userStoryDTO) {
        UserStoryDTO updatedUserStory = userStoryService.update(id, userStoryDTO);
        return ResponseEntity.ok(updatedUserStory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long id) {
        userStoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}