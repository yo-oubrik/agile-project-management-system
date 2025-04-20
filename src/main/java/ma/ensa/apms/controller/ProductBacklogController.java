package ma.ensa.apms.controller;

import java.util.List;

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
import ma.ensa.apms.dto.ProductBacklogRequest;
import ma.ensa.apms.dto.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryDTO;
import ma.ensa.apms.service.ProductBacklogService;

@RestController
@RequestMapping("/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;
    
    @PostMapping
    public ResponseEntity<ProductBacklogResponse> create(@Valid @RequestBody ProductBacklogRequest request) {
        return new ResponseEntity<>(productBacklogService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogResponse> getUserStoryById(@PathVariable Long id) {
        return ResponseEntity.ok(productBacklogService.getProductBacklogById(id));
    }

    @GetMapping("/{id}/sorted-user-stories")
    public ResponseEntity<List<UserStoryDTO>> getBacklogUserStoriesSorted(@PathVariable Long id) {
        List<UserStoryDTO> userStories = productBacklogService.getBacklogUserStoriesSorted(id);
        return ResponseEntity.ok(userStories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productBacklogService.deleteProductBacklog(id);
        return ResponseEntity.noContent().build();
    }
}
