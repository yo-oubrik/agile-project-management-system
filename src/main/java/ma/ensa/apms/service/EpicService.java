package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;

public interface EpicService {
    EpicResponse create(EpicRequest dto);

    EpicResponse findById(UUID id);

    List<EpicResponse> findAll();

    EpicResponse update(UUID id, EpicRequest dto);

    void delete(UUID id);

    List<UserStoryResponse> getUserStoriesByEpicId(UUID epicId);

    ProductBacklogResponse getProductBacklogByEpicId(UUID epicId);

    EpicResponse addUserStoryToEpic(UUID epicId, UUID userStoryId);

    long countEpics();
}
