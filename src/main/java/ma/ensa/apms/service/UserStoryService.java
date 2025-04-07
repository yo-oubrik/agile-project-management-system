package ma.ensa.apms.service;

import java.util.List;

import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.modal.enums.UserStoryStatus;

public interface UserStoryService {
    UserStoryDTO create(UserStoryCreationDTO dto);

    UserStoryDTO updateUserStory(Long id, UserStoryCreationDTO dto);

    UserStoryDTO getUserStoryById(Long id);

    UserStoryDTO changeStatus(Long id, UserStoryStatus newStatus);

    List<UserStoryDTO> getBacklogSorted(Long productBacklogId);

    UserStoryDTO linkToEpic(Long storyId, Long epicId);

    void delete(Long id);
}
