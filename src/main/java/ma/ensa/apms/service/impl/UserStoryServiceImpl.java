package ma.ensa.apms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.EmptyResourcesException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.UserStoryService;

@Service
@AllArgsConstructor
public class UserStoryServiceImpl implements UserStoryService {

    private UserStoryRepository userStoryRepository;
    private UserStoryMapper userStoryMapper;
    private ProductBacklogRepository productBacklogRepository;

    @Override
    @Transactional
    public UserStoryDTO create(UserStoryCreationDTO dto) {
        UserStory entity = userStoryMapper.toEntity(dto);

        if (dto.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(dto.getProductBacklogId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));
            entity.setProductBacklog(productBacklog);
        }

        entity = userStoryRepository.save(entity);
        return userStoryMapper.toDto(entity);
    }

    @Override
    public UserStoryDTO findById(Long id) {
        return userStoryRepository.findById(id)
                .map(userStoryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));
    }

    @Override
    public List<UserStoryDTO> findAll() {
        List<UserStoryDTO> userStoryDTOs = userStoryRepository.findAll()
                .stream()
                .map(userStoryMapper::toDto)
                .toList();

        if (userStoryDTOs.isEmpty()) {
            throw new EmptyResourcesException("No user stories found");
        }

        return userStoryDTOs;
    }

    @Override
    @Transactional
    public UserStoryDTO update(Long id, UserStoryCreationDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("User story ID is required");
        }

        UserStory existingEntity = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));

        userStoryMapper.updateEntityFromDto(dto, existingEntity);

        if (dto.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(dto.getProductBacklogId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));
            existingEntity.setProductBacklog(productBacklog);
        }

        existingEntity = userStoryRepository.save(existingEntity);
        return userStoryMapper.toDto(existingEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("User story not found");
        }
        userStoryRepository.deleteById(id);
    }
}
