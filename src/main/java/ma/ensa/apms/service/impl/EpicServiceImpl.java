package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.EpicService;

@Service
@RequiredArgsConstructor
public class EpicServiceImpl implements EpicService {

    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;
    private final EpicMapper epicMapper;
    private final UserStoryMapper userStoryMapper;
    private final ProductBacklogMapper productBacklogMapper;

    private int getUserStoriesCount(Epic epic) {
        return epic.getUserStories() != null ? epic.getUserStories().size() : 0;
    }

    @Override
    @Transactional
    public EpicResponse create(EpicRequest dto) {
        Epic epic = epicMapper.toEntity(dto);
        return epicMapper.toDto(epicRepository.save(epic));
    }

    @Override
    public EpicResponse findById(UUID id) {
        Epic epic = getEpicById(id);
        EpicResponse response = epicMapper.toDto(epic);
        response.setUserStoriesCount(getUserStoriesCount(epic));
        return response;
    }

    @Override
    public List<EpicResponse> findAll() {
        return epicRepository.findAll().stream()
                .map(epic -> {
                    EpicResponse response = epicMapper.toDto(epic);
                    response.setUserStoriesCount(getUserStoriesCount(epic));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EpicResponse update(UUID id, EpicRequest dto) {
        Epic epic = getEpicById(id);
        epicMapper.updateEntityFromDto(dto, epic);
        return epicMapper.toDto(epicRepository.save(epic));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Epic epic = getEpicById(id);
        epicRepository.delete(epic);
    }

    @Override
    @Transactional
    public EpicResponse addUserStoryToEpic(UUID epicId, UUID userStoryId) {
        Epic epic = getEpicById(epicId);
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory not found with id: " + userStoryId));

        userStory.setEpic(epic);
        userStoryRepository.save(userStory);

        return epicMapper.toDto(epic);
    }

    private Epic getEpicById(UUID id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found with id: " + id));
    }

    @Override
    public List<UserStoryResponse> getUserStoriesByEpicId(UUID epicId) {
        Epic epic = getEpicById(epicId);
        return epic.getUserStories().stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductBacklogResponse getProductBacklogByEpicId(UUID epicId) {
        Epic epic = getEpicById(epicId);
        if (epic.getProductBacklog() == null) {
            throw new ResourceNotFoundException("No product backlog is associated with epic id: " + epicId);
        }
        return productBacklogMapper.toDto(epic.getProductBacklog());
    }

    @Override
    public long countEpics() {
        return epicRepository.count();
    }
}
