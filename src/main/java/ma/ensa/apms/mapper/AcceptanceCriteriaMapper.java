package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.ensa.apms.dto.AcceptanceCriteriaCreationDTO;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.modal.AcceptanceCriteria;

@Mapper(componentModel = "spring")
public interface AcceptanceCriteriaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userStory", ignore = true)
    AcceptanceCriteria toEntity(AcceptanceCriteriaCreationDTO dto);

    AcceptanceCriteriaDTO toDto(AcceptanceCriteria entity);

    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AcceptanceCriteriaCreationDTO dto, @MappingTarget AcceptanceCriteria entity);
}