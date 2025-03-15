package ma.ensa.apms.service;

import java.util.List;

import ma.ensa.apms.dto.AcceptanceCriteriaCreationDTO;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;

public interface AcceptanceCriteriaService {
    AcceptanceCriteriaDTO create(AcceptanceCriteriaCreationDTO dto);

    AcceptanceCriteriaDTO findById(Long id);

    List<AcceptanceCriteriaDTO> findAll();

    public AcceptanceCriteriaDTO update(Long id, AcceptanceCriteriaCreationDTO dto);

    void delete(Long id);
}