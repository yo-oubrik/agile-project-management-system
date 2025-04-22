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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.service.AcceptanceCriteriaService;

@RestController
@RequestMapping("/acceptance-criteria")
@RequiredArgsConstructor
public class AcceptanceCriteriaController {

    private final AcceptanceCriteriaService acceptanceCriteriaService;

    @PostMapping
    public ResponseEntity<AcceptanceCriteriaResponse> create(@Valid @RequestBody AcceptanceCriteriaRequest dto) {
        return new ResponseEntity<>(acceptanceCriteriaService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcceptanceCriteriaResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(acceptanceCriteriaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AcceptanceCriteriaResponse>> finyAllByMet(@RequestParam Boolean met) {
        return ResponseEntity.ok(acceptanceCriteriaService.findAllByMet(met));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcceptanceCriteriaResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody AcceptanceCriteriaRequest dto) {
        return ResponseEntity.ok(acceptanceCriteriaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        acceptanceCriteriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/met")
    public ResponseEntity<AcceptanceCriteriaResponse> updateMet(@PathVariable UUID id, @RequestBody Boolean met) {
        return ResponseEntity.ok(acceptanceCriteriaService.updateMet(id, met));
    }

    @GetMapping("/{id}/user-story")
    public ResponseEntity<UserStoryResponse> getUserStory(@PathVariable UUID id) {
        return ResponseEntity.ok(acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id));
    }
}
