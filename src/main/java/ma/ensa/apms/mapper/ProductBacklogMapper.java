package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.modal.ProductBacklog;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductBacklogMapper {

    ProductBacklogResponse toResponse(ProductBacklog productBacklog);

    @Mapping(target = "id", ignore = true)
    ProductBacklog toEntity(ProductBacklogRequest productBacklogDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProductBacklogRequest dto, @MappingTarget ProductBacklog entity);
}
