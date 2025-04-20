package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.UserStoryDTO;
import ma.ensa.apms.modal.UserStory;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserStoryMapper {

    @Mapping(target = "id", ignore = true)
    UserStory toEntity(UserStoryRequest dto);

    UserStoryDTO toDto(UserStory entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserStoryRequest dto, @MappingTarget UserStory entity);
}
