package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.DuplicateResourceException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.UserStoryService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserStoryServiceImpl implements UserStoryService {
    private final UserStoryRepository userStoryRepository;
    private final UserStoryMapper userStoryMapper;

    @Override
    public UserStoryDTO save(UserStoryDTO userStoryDTO) {
        if (userStoryRepository.existsByNameAndProductBacklogId(
                userStoryDTO.getName(),
                userStoryDTO.getProductBacklogId())) {
            throw new DuplicateResourceException(
                    "User Story with name '" + userStoryDTO.getName() +
                            "' already exists in this Product Backlog");
        }

        UserStory userStory = userStoryMapper.toEntity(userStoryDTO);
        userStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(userStory);
    }

    @Override
    public UserStoryDTO findById(Long id) {
        return userStoryRepository.findById(id).map(userStoryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));
    }

    @Override
    public List<UserStoryDTO> findAll() {
        return userStoryRepository.findAll().stream().map(userStoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserStoryDTO update(Long id, UserStoryDTO userStoryDTO) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));
        userStoryMapper.updateEntityFromDto(userStoryDTO, userStory);
        userStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(userStory);
    }

    @Override
    public void delete(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("User Story not found");
        }
        userStoryRepository.deleteById(id);
    }
}