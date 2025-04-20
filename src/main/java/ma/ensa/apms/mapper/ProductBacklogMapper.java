package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.modal.ProductBacklog;

@Mapper(componentModel = "spring")
public interface ProductBacklogMapper {
    ProductBacklogResponse toResponse(ProductBacklog productBacklog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userStories", ignore = true)
    @Mapping(target = "epics", ignore = true)
    @Mapping(target = "project", ignore = true)
    ProductBacklog toEntity(ProductBacklogRequest productBacklogDTO);
}
