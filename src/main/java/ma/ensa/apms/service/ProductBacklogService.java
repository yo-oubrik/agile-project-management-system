package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;

public interface ProductBacklogService {
    ProductBacklogResponse create(ProductBacklogRequest productBacklogRequest);

    List<UserStoryResponse> getBacklogUserStoriesSorted(UUID productBacklogId);

    ProductBacklogResponse getProductBacklogById(UUID id);

    void deleteProductBacklog(UUID id);
}
