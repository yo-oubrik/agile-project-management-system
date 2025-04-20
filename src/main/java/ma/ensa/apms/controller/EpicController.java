package ma.ensa.apms.controller;

import java.util.List;
import java.util.UUID;

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
import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.UserStoryToEpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.service.EpicService;

@RestController
@RequestMapping("/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    @PostMapping
    public ResponseEntity<EpicResponse> create(@Valid @RequestBody EpicRequest dto) {
        return new ResponseEntity<>(epicService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpicResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(epicService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EpicResponse>> findAll() {
        return ResponseEntity.ok(epicService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpicResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody EpicRequest dto) {
        return ResponseEntity.ok(epicService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        epicService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{epicId}/user-stories")
    public ResponseEntity<EpicResponse> addUserStoryToEpic(
            @PathVariable UUID epicId,
            @Valid @RequestBody UserStoryToEpicRequest dto) {
        return new ResponseEntity<>(epicService.addUserStoryToEpic(epicId, dto.getUserStoryId()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/user-stories")
    public ResponseEntity<List<UserStoryResponse>> getUserStories(@PathVariable UUID id) {
        return ResponseEntity.ok(epicService.getUserStoriesByEpicId(id));
    }

    @GetMapping("/{id}/product-backlog")
    public ResponseEntity<ProductBacklogResponse> getProductBacklog(@PathVariable UUID id) {
        return ResponseEntity.ok(epicService.getProductBacklogByEpicId(id));
    }
}