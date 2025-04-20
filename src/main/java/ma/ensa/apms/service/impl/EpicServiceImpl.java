package ma.ensa.apms.service.impl;

import java.util.List;

import ma.ensa.apms.mapper.EpicMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.exception.EmptyResourcesException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.service.EpicService;

@Service
@AllArgsConstructor
public class EpicServiceImpl implements EpicService {

    private final EpicRepository epicRepository;
    private final EpicMapper epicMapper;

    @Override
    @Transactional
    public EpicDTO create(EpicCreationDTO dto) {
        Epic entity = epicMapper.toEntity(dto);
        entity = epicRepository.save(entity);
        return epicMapper.toDto(entity);
    }

    @Override
    public EpicDTO findById(Long id) {
        return epicRepository.findById(id)
                .map(epicMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));
    }

    @Override
    public List<EpicDTO> findAll() {
        List<EpicDTO> epicDTOs = epicRepository.findAll()
                .stream()
                .map(epicMapper::toDto)
                .toList();

        if (epicDTOs.isEmpty()) {
            throw new EmptyResourcesException("No epics found");
        }

        return epicDTOs;
    }

    @Override
    @Transactional
    public EpicDTO update(Long id, EpicCreationDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("Epic ID is required");
        }

        Epic existingEntity = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));

        epicMapper.updateEntityFromDto(dto, existingEntity);
        existingEntity = epicRepository.save(existingEntity);
        return epicMapper.toDto(existingEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!epicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Epic not found");
        }
        epicRepository.deleteById(id);
    }
}
