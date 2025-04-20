package ma.ensa.apms.service;

import java.util.List;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.UserStoryDTO;
import ma.ensa.apms.modal.enums.UserStoryStatus;

public interface UserStoryService {
    UserStoryDTO create(UserStoryRequest dto);

    UserStoryDTO updateUserStory(Long id, UserStoryRequest dto);

    UserStoryDTO getUserStoryById(Long id);

    UserStoryDTO changeStatus(Long id, UserStoryStatus newStatus);

    UserStoryDTO linkToEpic(Long storyId, Long epicId);

    List<UserStoryDTO> getUserStoriesByStatusAndProductBacklogId(UserStoryStatus statut , Long productBacklogId);
    List<UserStoryDTO> getUserStoriesByEpicId(Long epicId);
    List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintId);

    void delete(Long id);
}
