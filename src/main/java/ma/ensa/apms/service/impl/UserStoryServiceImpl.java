package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.UserStoryResponse;
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
     * 
     * @param dto the user story data to create
     * @return the created user story
     * @throws ResourceNotFoundException if the product backlog is not found
     */
    @Override
    @Transactional
    public UserStoryResponse create(UserStoryRequest dto) {
        UserStory us = userStoryMapper.toEntity(dto);

        if (dto.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(dto.getProductBacklogId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));
            us.setProductBacklog(productBacklog);
        }
        us.setStatus(UserStoryStatus.TODO);
        userStoryRepository.save(us);
        return userStoryMapper.toResponse(us);
    }

    /**
     * Update an existing user story
     * 
     * @param id  the id of the user story
     * @param dto the user story data to update
     * @return the updated user story
     * @throws ResourceNotFoundException if the user story is not found
     */
    @Override
    @Transactional
    public UserStoryResponse updateUserStory(UUID id, UserStoryRequest dto) {
        if (id == null) {
            // log.error("User story ID is null");
            throw new IllegalArgumentException("User story ID is required");
        }

        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));

        userStoryMapper.updateEntityFromDto(dto, us);

        if (dto.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(dto.getProductBacklogId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));
            us.setProductBacklog(productBacklog);
        }
        userStoryRepository.save(us);
        return userStoryMapper.toResponse(us);
    }

    /**
     * get user story by id
     * 
     * @param id the id of the user story
     * @return the user story
     * @throws ResourceNotFoundException if the user story is not found
     */
    @Override
    public UserStoryResponse getUserStoryById(UUID id) {
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));
        return userStoryMapper.toResponse(us);
    }

    /**
     * Change the status of a user story
     * 
     * @param id        the id of the user story
     * @param newStatus the new status of the user story
     * @return the updated user story
     * @throws ResourceNotFoundException if the user story is not found
     */
    @Override
    @Transactional
    public UserStoryResponse changeStatus(UUID id, UserStoryStatus newStatus) {
        UserStory story = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));

        // verifier les criteres d'acceptance
        if (newStatus == UserStoryStatus.DONE) {
            boolean allMet = story.getAcceptanceCriterias().stream()
                    .allMatch(AcceptanceCriteria::isMet);

            if (!allMet) {
                throw new BusinessException("All acceptance criteria must be met to mark as DONE.");
            }
        }

        story.setStatus(newStatus);
        return userStoryMapper.toResponse(userStoryRepository.save(story));
    }

    /**
     * Link a User Story to an Epic
     * 
     * @param storyId the id of the user story to link
     * @param epicId  the id of the epic to link
     * @return the linked user story
     * @throws ResourceNotFoundException if the user story or epic is not found
     * @throws BusinessException         if the user story is not in TODO status
     */
    @Override
    @Transactional
    public UserStoryResponse linkToEpic(UUID storyId, UUID epicId) {
        UserStory story = userStoryRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));
        if (story.getStatus() != UserStoryStatus.TODO) {
            throw new BusinessException("Cannot link an epic to a user story with status higher than TODO");
        }
        story.setEpic(epic);
        return userStoryMapper.toResponse(userStoryRepository.save(story));
    }

    /**
     * Move a user story to a sprint
     * 
     * @param usId     the id of the user story to move
     * @param sprintId the id of the sprint to move to
     * @return the moved user story
     * @throws ResourceNotFoundException if the user story or sprint is not found
     */
    // @Override
    // @Transactional
    // public UserStoryResponse moveToSprint(UUID usId, UUID sprintId) {
    // UserStory us = userStoryRepository.findById(usId)
    // .orElseThrow(() -> new RuntimeException("US introuvable"));
    // Sprint sprint = sprintRepository.findById(sprintId)
    // .orElseThrow(() -> new RuntimeException("Sprint introuvable"));
    // us.setSprint(sprint);
    // return userStoryMapper.toDTO(userStoryRepository.save(us));
    // }

    /**
     * Get all user stories by status
     * 
     * @param statut the status of the user stories to get
     * @return the list of user stories with the given status
     */
    @Override
    public List<UserStoryResponse> getUserStoriesByStatusAndProductBacklogId(UserStoryStatus statut , UUID productBacklogId) {
        if (statut == null) {
            throw new IllegalArgumentException("Status is required");
        }
        if (productBacklogId == null) {
            throw new IllegalArgumentException("Product Backlog ID is required");
        }
        productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog not found"));
        return userStoryRepository.findByStatusAndProductBacklogId(statut, productBacklogId)
                .stream()
                .map(userStoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all user stories by epic
     * 
     * @param epicId the id of the epic to get user stories for
     * @return the list of user stories linked to the given epic
     */
    @Override
    public List<UserStoryResponse> getUserStoriesByEpicId(UUID epicId) {
        if (epicId == null) {
            throw new IllegalArgumentException("Epic ID is required");
        }
        epicRepository.findById(epicId)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));
        return userStoryRepository.findByEpicId(epicId)
                .stream()
                .map(userStoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all user stories by sprint
     * 
     * @param sprintId the id of the sprint to get user stories for
     * @return the list of user stories linked to the given sprint
     */
    @Override
    public List<UserStoryResponse> getUserStoriesBySprintBacklogId(UUID sprintId) {
        if (sprintId == null) {
            throw new IllegalArgumentException("Sprint ID is required");
        }
        // sprintRepository.findById(sprintId)
        // .orElseThrow(() -> new ResourceNotFoundException("Sprint not found"));
        return userStoryRepository.findBySprintBacklogId(sprintId)
                .stream()
                .map(userStoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Delete a user story
     * 
     * @param id the id of the user story to delete
     * @throws ResourceNotFoundException if the user story is not found
     * @throws BusinessException         if the user story is not in TODO status
     */
    @Override
    @Transactional
    public void delete(UUID id) {
        UserStory story = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found"));
        if (story.getStatus() != UserStoryStatus.TODO) {
            throw new BusinessException("Only stories in TODO state can be deleted.");
        }
        userStoryRepository.deleteById(id);
    }
}
