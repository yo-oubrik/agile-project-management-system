package ma.ensa.apms.service;

import java.util.List;

import ma.ensa.apms.dto.ProductBacklogRequest;
import ma.ensa.apms.dto.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryDTO;

public interface ProductBacklogService {
    ProductBacklogResponse create(ProductBacklogRequest productBacklogRequest);
    List<UserStoryDTO> getBacklogUserStoriesSorted(Long productBacklogId);
    ProductBacklogResponse getProductBacklogById(Long id);
    void deleteProductBacklog(Long id);
}
