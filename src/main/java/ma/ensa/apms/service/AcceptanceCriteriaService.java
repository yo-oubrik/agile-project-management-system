package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;

public interface AcceptanceCriteriaService {
    AcceptanceCriteriaResponse create(AcceptanceCriteriaRequest dto);

    AcceptanceCriteriaResponse findById(UUID id);

    List<AcceptanceCriteriaResponse> findAll();

    public AcceptanceCriteriaResponse update(UUID id, AcceptanceCriteriaRequest dto);

    void delete(UUID id);
}