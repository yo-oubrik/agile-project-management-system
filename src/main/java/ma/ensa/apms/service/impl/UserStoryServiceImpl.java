package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.UserStoryService;


@Service
@AllArgsConstructor
// @Slf4j
public class UserStoryServiceImpl implements UserStoryService {

    private UserStoryRepository userStoryRepository;
    private UserStoryMapper userStoryMapper;
    private ProductBacklogRepository productBacklogRepository;
    private EpicRepository epicRepository;

    /**
     * Create a new user story
     * @param dto the user story data to create
     * @return the created user story
     * @throws ResourceNotFoundException if the product backlog is not found
     */
    @Override
    @Transactional
    public UserStoryDTO create(UserStoryCreationDTO dto) {
        // log.info("Creating user story: [{}]", dto);
        UserStory us = userStoryMapper.toEntity(dto);

        if (dto.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(dto.getProductBacklogId())
                    .orElseThrow(() -> {
                        // log.error("Product backlog with ID = {} not found", dto.getProductBacklogId());
                        return new ResourceNotFoundException("Product backlog not found");
                    });
            us.setProductBacklog(productBacklog);
        }
        us.setStatus(UserStoryStatus.TODO);
        userStoryRepository.save(us);
        // log.info("User story created with ID ={}", us.getId());
        return userStoryMapper.toDto(us);
    }

    /**
     * Update an existing user story
     * @param id the id of the user story
     * @param dto the user story data to update
     * @return the updated user story
     * @throws ResourceNotFoundException if the user story is not found
     */
    @Override
    @Transactional
    public UserStoryDTO updateUserStory(Long id, UserStoryCreationDTO dto) {
        // log.info("Updating user story with ID = {} and data = [{}]", id, dto);
        if (id == null) {
            // log.error("User story ID is null");
            throw new IllegalArgumentException("User story ID is required");
        }

        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> {
                    // log.error("User story with ID {} not found", id);
                    return new ResourceNotFoundException("User story not found");
                });

        userStoryMapper.updateEntityFromDto(dto, us);

        if (dto.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(dto.getProductBacklogId())
                    .orElseThrow(() -> {
                        // log.error("Product backlog with ID = {} not found", dto.getProductBacklogId());
                        return new ResourceNotFoundException("Product backlog not found");
                    });
            us.setProductBacklog(productBacklog);
        }
        userStoryRepository.save(us);
        // log.info("User story updated with ID = {}", id);
        return userStoryMapper.toDto(us);
    }

    /**
     * get user story by id
     * @param id the id of the user story
     * @return the user story
     * @throws ResourceNotFoundException if the user story is not found
     */
    @Override
    public UserStoryDTO getUserStoryById(Long id) {
        // log.info("Loading user story with ID = {}", id);
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> {
                    // log.error("User story with ID = {} not found", id);
                    return new ResourceNotFoundException("User story not found");
                });
        // log.info("User story loaded with ID = {}", id);
        return userStoryMapper.toDto(us);
    }

    /**
     * Change the status of a user story
     * @param id the id of the user story
     * @param newStatus the new status of the user story
     * @return the updated user story
     * @throws ResourceNotFoundException if the user story is not found
     */
    @Override
    @Transactional
    public UserStoryDTO changeStatus(Long id, UserStoryStatus newStatus) {
        // log.info("Changing status of user story with ID = {} to {}", id, newStatus);
        UserStory story = userStoryRepository.findById(id)
                .orElseThrow(() -> {
                    // log.error("User story with ID = {} not found", id);
                    return new ResourceNotFoundException("User story not found");
                });

        // verifier les criteres d'acceptance
        if (newStatus == UserStoryStatus.DONE) {
            boolean allMet = story.getAcceptanceCriterias().stream()
                                .allMatch(AcceptanceCriteria::isMet);

            if (!allMet) {
                // log.error("Not all acceptance criteria met for user story with ID = {}", id);
                throw new BusinessException("All acceptance criteria must be met to mark as DONE.");
            }
        }

        story.setStatus(newStatus);
        // log.info("User story with ID = {} status changed to {}", id, newStatus);
        return userStoryMapper.toDto(userStoryRepository.save(story));
    }

    /**
     * Return the list of user stories sorted by priority
     * @param productBacklog the product backlog to which the user stories belong
     * @return the list of user stories sorted by priority
     * @throws IllegalArgumentException if the product backlog is null
     * @throws ResourceNotFoundException if the product backlog is not found
     */
    @Override
    public List<UserStoryDTO> getBacklogSorted(Long productBacklogId) {
        // log.info("Loading all user stories sorted by priority for product backlog with ID = {}", productBacklogId);
        if (productBacklogId == null) {
            // log.error("Product backlog ID is null");
            throw new IllegalArgumentException("Product backlog is required");
        }
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
            .orElseThrow(() -> {
                // log.error("Product backlog with ID = {} not found", productBacklogId);
                return new ResourceNotFoundException("Product backlog not found");
            });
        // log.info("Product backlog with ID = {} found", productBacklogId);
        return userStoryRepository.findByProductBacklogOrderByPriorityDesc(productBacklog).stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Link a User Story to an Epic
     * @param storyId the id of the user story to link
     * @param epicId the id of the epic to link
     * @return the linked user story
     * @throws ResourceNotFoundException if the user story or epic is not found
     * @throws BusinessException if the user story is not in TODO status
     */
    @Override
    @Transactional
    public UserStoryDTO linkToEpic(Long storyId, Long epicId) {
        // log.info("Linking user story with ID = {} to epic with ID = {}", storyId, epicId);
        UserStory story = userStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));
        if(story.getStatus() != UserStoryStatus.TODO ){
            // log.error("Cannot link an epic to a user story with status higher than TODO");
            throw new BusinessException("Cannot link an epic to a user story with status higher than TODO");
        }
        // log.info("User story with ID = {} found", storyId);
        Epic epic = epicRepository.findById(epicId).orElseThrow(() -> {
            // log.error("Epic with ID = {} not found", epicId);
            return new ResourceNotFoundException("Epic not found");
        });
        // log.info("Epic with ID = {} found", epicId);
        story.setEpic(epic);
        // log.info("User story with ID = {} linked to epic with ID = {}", storyId, epicId);
        return userStoryMapper.toDto(userStoryRepository.save(story));
    }

    /**
     * Delete a user story
     * @param id the id of the user story to delete
     * @throws ResourceNotFoundException if the user story is not found
     * @throws BusinessException if the user story is not in TODO status
     */
    @Override
    @Transactional
    public void delete(Long id) {
        // log.info("Deleting user story with ID = {}", id);
        UserStory story = userStoryRepository.findById(id)
                .orElseThrow(() -> {
                    // log.error("User Story with ID = {} not found", id);
                    return new ResourceNotFoundException("User Story not found");
                });
        if (story.getStatus() != UserStoryStatus.TODO) {
            // log.error("Cannot delete a user story with status higher than TODO");
            throw new BusinessException("Only stories in TODO state can be deleted.");
        }
        // log.info("User story with ID = {} found", id);
        userStoryRepository.deleteById(id);
    }
}
