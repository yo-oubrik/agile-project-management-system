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
import ma.ensa.apms.dto.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
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
    public ResponseEntity<AcceptanceCriteriaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(acceptanceCriteriaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AcceptanceCriteriaResponse>> findAll() {
        return ResponseEntity.ok(acceptanceCriteriaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcceptanceCriteriaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AcceptanceCriteriaRequest dto) {
        return ResponseEntity.ok(acceptanceCriteriaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        acceptanceCriteriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
