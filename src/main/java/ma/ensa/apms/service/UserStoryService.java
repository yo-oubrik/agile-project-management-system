package ma.ensa.apms.service;

import java.util.List;

import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;

public interface UserStoryService {
    UserStoryDTO create(UserStoryCreationDTO dto);

    UserStoryDTO findById(Long id);

    List<UserStoryDTO> findAll();

    UserStoryDTO update(Long id, UserStoryCreationDTO dto);

    void delete(Long id);
}
