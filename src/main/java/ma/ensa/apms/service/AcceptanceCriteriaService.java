package ma.ensa.apms.service;

import java.util.List;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;

public interface AcceptanceCriteriaService {
    AcceptanceCriteriaDTO save(AcceptanceCriteriaDTO criteriaDTO);

    AcceptanceCriteriaDTO findById(Long id);

    List<AcceptanceCriteriaDTO> findAll();

    AcceptanceCriteriaDTO update(Long id, AcceptanceCriteriaDTO criteriaDTO);

    void delete(Long id);
}