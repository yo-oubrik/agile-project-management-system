package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.AcceptanceCriteriaService;

@Service
@AllArgsConstructor
public class AcceptanceCriteriaServiceImpl implements AcceptanceCriteriaService {

    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;
    private AcceptanceCriteriaMapper acceptanceCriteriaMapper;
    private UserStoryRepository userStoryRepository;

    @Override
    @Transactional
    public AcceptanceCriteriaResponse create(AcceptanceCriteriaRequest dto) {
        AcceptanceCriteria entity = acceptanceCriteriaMapper.toEntity(dto);

        if (dto.getUserStoryId() != null) {
            UserStory userStory = userStoryRepository.findById(dto.getUserStoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("User story not found"));
            entity.setUserStory(userStory);
        }

        entity = acceptanceCriteriaRepository.save(entity);
        return acceptanceCriteriaMapper.toDto(entity);
    }

    @Override
    public AcceptanceCriteriaResponse findById(UUID id) {
        return acceptanceCriteriaRepository.findById(id)
                .map(acceptanceCriteriaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));
    }

    @Override
    public List<AcceptanceCriteriaResponse> findAll() {
        List<AcceptanceCriteriaResponse> acceptanceCriteriaDTOs = acceptanceCriteriaRepository.findAll()
                .stream()
                .map(acceptanceCriteriaMapper::toDto)
                .toList();

        return acceptanceCriteriaDTOs;
    }

    @Override
    @Transactional
    public AcceptanceCriteriaResponse update(UUID id, AcceptanceCriteriaRequest dto) {
        if (id == null) {
            throw new IllegalArgumentException("Acceptance Criteria ID is required");
        }

        AcceptanceCriteria existingEntity = acceptanceCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));

        acceptanceCriteriaMapper.updateEntityFromDto(dto, existingEntity);

        if (dto.getUserStoryId() != null) {
            UserStory userStory = userStoryRepository.findById(dto.getUserStoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("User story not found"));
            existingEntity.setUserStory(userStory);
        }

        existingEntity = acceptanceCriteriaRepository.save(existingEntity);
        return acceptanceCriteriaMapper.toDto(existingEntity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!acceptanceCriteriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Acceptance Criteria not found");
        }
        acceptanceCriteriaRepository.deleteById(id);
    }
}