package ma.ensa.apms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.service.EpicService;

@RestController
@RequestMapping("/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    @PostMapping
    public ResponseEntity<EpicDTO> create(@Valid @RequestBody EpicCreationDTO dto) {
        return new ResponseEntity<>(epicService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpicDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(epicService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EpicDTO>> findAll() {
        return ResponseEntity.ok(epicService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpicDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EpicCreationDTO dto) {
        return ResponseEntity.ok(epicService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        epicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}