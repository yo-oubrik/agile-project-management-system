package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;

public interface UserStoryService {
    UserStoryResponse create(UserStoryRequest dto);

    UserStoryResponse updateUserStory(UUID id, UserStoryRequest dto);

    UserStoryResponse getUserStoryById(UUID id);

    UserStoryResponse changeStatus(UUID id, UserStoryStatus newStatus);

    UserStoryResponse linkToEpic(UUID storyId, UUID epicId);

    List<UserStoryResponse> getUserStoriesByStatusAndProductBacklogId(UserStoryStatus statut, UUID productBacklogId);

    List<UserStoryResponse> getUserStoriesByEpicId(UUID epicId);

    List<UserStoryResponse> getUserStoriesBySprintBacklogId(UUID sprintId);

    void delete(UUID id);
}
