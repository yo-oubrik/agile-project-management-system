package ma.ensa.apms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.AcceptanceCriteriaCreationDTO;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.exception.EmptyResourcesException;
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
    public AcceptanceCriteriaDTO create(AcceptanceCriteriaCreationDTO dto) {
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
    public AcceptanceCriteriaDTO findById(Long id) {
        return acceptanceCriteriaRepository.findById(id)
                .map(acceptanceCriteriaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));
    }

    @Override
    public List<AcceptanceCriteriaDTO> findAll() {
        List<AcceptanceCriteriaDTO> acceptanceCriteriaDTOs = acceptanceCriteriaRepository.findAll()
                .stream()
                .map(acceptanceCriteriaMapper::toDto)
                .toList();

        if (acceptanceCriteriaDTOs.isEmpty()) {
            throw new EmptyResourcesException("No acceptance criteria found");
        }

        return acceptanceCriteriaDTOs;
    }

    @Override
    @Transactional
    public AcceptanceCriteriaDTO update(Long id, AcceptanceCriteriaCreationDTO dto) {
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
    public void delete(Long id) {
        if (!acceptanceCriteriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Acceptance Criteria not found");
        }
        acceptanceCriteriaRepository.deleteById(id);
    }
}