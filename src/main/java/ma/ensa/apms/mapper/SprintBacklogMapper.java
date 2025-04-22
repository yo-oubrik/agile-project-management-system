package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ma.ensa.apms.dto.Request.SprintBacklogRequest;
import ma.ensa.apms.dto.Response.SprintBacklogResponse;
import ma.ensa.apms.modal.SprintBacklog;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SprintBacklogMapper {

    @Mapping(target = "id", ignore = true)
    SprintBacklog toEntity(SprintBacklogRequest request);

    SprintBacklogResponse toResponse(SprintBacklog sprintBacklog);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(SprintBacklogRequest request, @MappingTarget SprintBacklog sprintBacklog);
}