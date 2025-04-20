package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.UserStoryDTO;
import ma.ensa.apms.modal.enums.UserStoryStatus;

public interface UserStoryService {
    UserStoryDTO create(UserStoryRequest dto);

    UserStoryDTO updateUserStory(UUID id, UserStoryRequest dto);

    UserStoryDTO getUserStoryById(UUID id);

    UserStoryDTO changeStatus(UUID id, UserStoryStatus newStatus);

    UserStoryDTO linkToEpic(UUID storyId, UUID epicId);

    List<UserStoryDTO> getUserStoriesByStatusAndProductBacklogId(UserStoryStatus statut , UUID productBacklogId);
    List<UserStoryDTO> getUserStoriesByEpicId(UUID epicId);
    List<UserStoryDTO> getUserStoriesBySprintBacklogId(UUID sprintId);

    void delete(UUID id);
}
