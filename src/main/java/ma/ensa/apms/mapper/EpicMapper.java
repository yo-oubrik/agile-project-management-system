package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.modal.Epic;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpicMapper {
    @Mapping(target = "id", ignore = true)
    Epic toEntity(EpicRequest dto);

    EpicResponse toDto(Epic entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(EpicRequest dto, @MappingTarget Epic entity);
}