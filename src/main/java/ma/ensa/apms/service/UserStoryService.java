package ma.ensa.apms.service;

import java.util.List;
import ma.ensa.apms.dto.UserStoryDTO;

public interface UserStoryService {
    UserStoryDTO save(UserStoryDTO userStoryDTO);

    UserStoryDTO findById(Long id);

    List<UserStoryDTO> findAll();

    UserStoryDTO update(Long id, UserStoryDTO userStoryDTO);

    void delete(Long id);
}