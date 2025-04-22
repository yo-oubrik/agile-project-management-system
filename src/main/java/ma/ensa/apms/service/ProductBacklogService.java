package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;

public interface ProductBacklogService {

    ProductBacklogResponse create(ProductBacklogRequest productBacklogRequest);

    ProductBacklogResponse getProductBacklogById(UUID id);

    void deleteProductBacklog(UUID id);

    // getUserStoryCount(UUID id);

    List<ProductBacklogResponse> getAllProductBacklogs();

    public List<UserStoryResponse> getUserStoriesByProductBacklogId(UUID productBacklogId);

    public List<EpicResponse> getEpicsByProductBacklogId(UUID productBacklogId);

    EpicResponse addEpicToProductBacklog(UUID productBacklogId, EpicRequest epicRequest);

    UserStoryResponse addUserStoryToProductBacklog(UUID productBacklogId, UserStoryRequest userStoryRequest);

    ProjectResponse getProjectByProductBacklogId(UUID productBacklogId);

}
