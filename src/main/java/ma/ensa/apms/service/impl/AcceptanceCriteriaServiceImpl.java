package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;
import ma.ensa.apms.service.AcceptanceCriteriaService;

@Service
@AllArgsConstructor
public class AcceptanceCriteriaServiceImpl implements AcceptanceCriteriaService {

    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;
    private AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @Override
    @Transactional
    public AcceptanceCriteriaResponse create(AcceptanceCriteriaRequest dto) {
        AcceptanceCriteria entity = acceptanceCriteriaMapper.toEntity(dto);
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
    public List<AcceptanceCriteriaResponse> findAllByMet(Boolean met) {
        return acceptanceCriteriaRepository.findByMet(met)
                .stream()
                .map(acceptanceCriteriaMapper::toDto)
                .toList();
    }

    @Override
    public List<AcceptanceCriteriaResponse> findAll() {
        return acceptanceCriteriaRepository.findAll()
                .stream()
                .map(acceptanceCriteriaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AcceptanceCriteriaResponse update(UUID id, AcceptanceCriteriaRequest dto) {
        AcceptanceCriteria existingEntity = acceptanceCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));

        acceptanceCriteriaMapper.updateEntityFromDto(dto, existingEntity);

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

    @Override
    @Transactional
    public AcceptanceCriteriaResponse updateMet(UUID id, Boolean met) {
        AcceptanceCriteria entity = acceptanceCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));
        entity.setMet(met);
        entity = acceptanceCriteriaRepository.save(entity);
        return acceptanceCriteriaMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStoryResponse getUserStoryByAcceptanceCriteriaId(UUID acceptanceCriteriaId) {
        AcceptanceCriteria acceptanceCriteria = acceptanceCriteriaRepository.findById(acceptanceCriteriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));

        UserStory userStory = acceptanceCriteria.getUserStory();
        if (userStory == null) {
            throw new ResourceNotFoundException("User Story not found for this Acceptance Criteria");
        }

        return UserStoryResponse.builder().build();
    }

}