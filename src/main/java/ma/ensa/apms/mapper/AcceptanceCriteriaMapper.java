package ma.ensa.apms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.modal.AcceptanceCriteria;

@Mapper(componentModel = "spring")
public interface AcceptanceCriteriaMapper {

    AcceptanceCriteria toEntity(AcceptanceCriteriaDTO dto);

    AcceptanceCriteriaDTO toDto(AcceptanceCriteria entity);

    void updateEntityFromDto(AcceptanceCriteriaDTO dto, @MappingTarget AcceptanceCriteria entity);

    List<AcceptanceCriteriaDTO> toDtoList(List<AcceptanceCriteria> entities);
}