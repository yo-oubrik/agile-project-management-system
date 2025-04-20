package ma.ensa.apms.service;

import java.util.List;

import ma.ensa.apms.dto.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;

public interface AcceptanceCriteriaService {
    AcceptanceCriteriaResponse create(AcceptanceCriteriaRequest dto);

    AcceptanceCriteriaResponse findById(Long id);

    List<AcceptanceCriteriaResponse> findAll();

    public AcceptanceCriteriaResponse update(Long id, AcceptanceCriteriaRequest dto);

    void delete(Long id);
}