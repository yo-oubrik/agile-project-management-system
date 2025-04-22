package ma.ensa.apms.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.service.ProductBacklogService;

@RestController
@RequestMapping("/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;

    @PostMapping
    public ResponseEntity<ProductBacklogResponse> createProductBacklog(
            @Valid @RequestBody ProductBacklogRequest request) {
        ProductBacklogResponse response = productBacklogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogResponse> getProductBacklogById(@PathVariable UUID id) {
        ProductBacklogResponse response = productBacklogService.getProductBacklogById(id);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductBacklog(@PathVariable UUID id) {
        productBacklogService.deleteProductBacklog(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product backlog deleted successfully.");

    }

    @GetMapping
    public ResponseEntity<List<ProductBacklogResponse>> getAllProductBacklogs() {
        List<ProductBacklogResponse> response = productBacklogService.getAllProductBacklogs();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/user-stories")
    public ResponseEntity<List<UserStoryResponse>> getUserStoriesByProductBacklogId(@PathVariable UUID id) {
        List<UserStoryResponse> userStories = productBacklogService.getUserStoriesByProductBacklogId(id);
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/{id}/epics")
    public ResponseEntity<List<EpicResponse>> getEpicsByProductBacklogId(@PathVariable UUID id) {
        List<EpicResponse> epics = productBacklogService.getEpicsByProductBacklogId(id);
        return ResponseEntity.ok(epics);
    }

    @PostMapping("/{id}/epics")
    public ResponseEntity<EpicResponse> addEpicToProductBacklog(
            @PathVariable UUID id, @Valid @RequestBody EpicRequest epicRequest) {
        EpicResponse epicResponse = productBacklogService.addEpicToProductBacklog(id, epicRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(epicResponse);
    }

    @PostMapping("/{id}/user-stories")
    public ResponseEntity<UserStoryResponse> addUserStoryToProductBacklog(
            @PathVariable UUID id, @Valid @RequestBody UserStoryRequest userStoryRequest) {
        UserStoryResponse userStoryResponse = productBacklogService.addUserStoryToProductBacklog(id, userStoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userStoryResponse);
    }

    @GetMapping("/{id}/project")
    public ResponseEntity<ProjectResponse> getProjectByProductBacklogId(@PathVariable UUID id) {
        ProjectResponse projectResponse = productBacklogService.getProjectByProductBacklogId(id);
        return ResponseEntity.ok(projectResponse);
    }

}