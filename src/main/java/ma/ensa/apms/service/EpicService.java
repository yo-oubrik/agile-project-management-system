package ma.ensa.apms.service;

import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;

import java.util.List;

public interface EpicService {
    EpicDTO create(EpicCreationDTO dto);

    EpicDTO findById(Long id);

    List<EpicDTO> findAll();

    EpicDTO update(Long id, EpicCreationDTO dto);

    void delete(Long id);
}
