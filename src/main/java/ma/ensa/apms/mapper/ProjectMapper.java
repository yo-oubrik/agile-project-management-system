package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.modal.Project;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    @Mapping(target = "id" , ignore = true)
    Project toEntity(ProjectRequest request);

    ProjectResponse toResponse(Project entity); 
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(ProjectRequest request, @MappingTarget Project entity);
}
