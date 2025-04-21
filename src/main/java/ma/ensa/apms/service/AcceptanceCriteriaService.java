package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;

public interface AcceptanceCriteriaService {
    AcceptanceCriteriaResponse create(AcceptanceCriteriaRequest dto);

    AcceptanceCriteriaResponse findById(UUID id);

    List<AcceptanceCriteriaResponse> findAllByMet(Boolean met);

    AcceptanceCriteriaResponse update(UUID id, AcceptanceCriteriaRequest dto);

    AcceptanceCriteriaResponse updateMet(UUID id, Boolean met);

    void delete(UUID id);

    UserStoryResponse getUserStoryByAcceptanceCriteriaId(UUID acceptanceCriteriaId);
}