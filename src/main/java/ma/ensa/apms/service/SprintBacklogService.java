package ma.ensa.apms.service;

import ma.ensa.apms.dto.Request.SprintBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.SprintBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;

import java.util.List;
import java.util.UUID;

public interface SprintBacklogService {
    SprintBacklogResponse createSprintBacklog(SprintBacklogRequest request);

    SprintBacklogResponse getSprintBacklogById(UUID id);

    List<SprintBacklogResponse> getAllSprintBacklogs();

    SprintBacklogResponse updateSprintBacklog(UUID id, SprintBacklogRequest request);

    void deleteSprintBacklog(UUID id);

    List<UserStoryResponse> getUserStoriesBySprintBacklogId(UUID sprintBacklogId);

    UserStoryResponse addUserStoryToSprintBacklog(UUID sprintBacklogId, UserStoryRequest userStoryRequest);

    void removeUserStoryFromSprintBacklog(UUID sprintBacklogId, UUID userStoryId);

}