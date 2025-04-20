package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.modal.AcceptanceCriteria;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AcceptanceCriteriaMapper {

    @Mapping(target = "id", ignore = true)
    AcceptanceCriteria toEntity(AcceptanceCriteriaRequest dto);

    AcceptanceCriteriaResponse toDto(AcceptanceCriteria entity);

    @Mapping(target = "userStory", ignore = true)
    void updateEntityFromDto(AcceptanceCriteriaRequest dto, @MappingTarget AcceptanceCriteria entity);
}