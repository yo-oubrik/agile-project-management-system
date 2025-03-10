package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;
import ma.ensa.apms.service.AcceptanceCriteriaService;

@Service
@Transactional
@RequiredArgsConstructor
public class AcceptanceCriteriaServiceImpl implements AcceptanceCriteriaService {

    private final AcceptanceCriteriaRepository acceptanceCriteriaRepository;
    private final AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @Override
    public AcceptanceCriteriaDTO save(AcceptanceCriteriaDTO criteriaDTO) {
        var criteria = acceptanceCriteriaMapper.toEntity(criteriaDTO);
        criteria = acceptanceCriteriaRepository.save(criteria);
        return acceptanceCriteriaMapper.toDto(criteria);
    }

    @Override
    public AcceptanceCriteriaDTO findById(Long id) {
        return acceptanceCriteriaRepository.findById(id)
                .map(acceptanceCriteriaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));
    }

    @Override
    public List<AcceptanceCriteriaDTO> findAll() {
        return acceptanceCriteriaRepository.findAll().stream()
                .map(acceptanceCriteriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AcceptanceCriteriaDTO update(Long id, AcceptanceCriteriaDTO criteriaDTO) {
        var criteria = acceptanceCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));
        acceptanceCriteriaMapper.updateEntityFromDto(criteriaDTO, criteria);
        criteria = acceptanceCriteriaRepository.save(criteria);
        return acceptanceCriteriaMapper.toDto(criteria);
    }

    @Override
    public void delete(Long id) {
        if (!acceptanceCriteriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Acceptance Criteria not found");
        }
        acceptanceCriteriaRepository.deleteById(id);
    }
}