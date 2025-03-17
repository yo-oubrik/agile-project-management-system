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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
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
    public ResponseEntity<UserStoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userStoryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserStoryDTO>> findAll() {
        return ResponseEntity.ok(userStoryService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserStoryCreationDTO dto) {
        return ResponseEntity.ok(userStoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userStoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}