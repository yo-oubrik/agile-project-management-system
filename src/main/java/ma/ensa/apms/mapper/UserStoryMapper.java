package ma.ensa.apms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.modal.UserStory;

@Mapper(componentModel = "spring")
public interface UserStoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "epic", ignore = true)
    UserStory toEntity(UserStoryCreationDTO dto);

    @Mapping(target = "productBacklogId", source = "productBacklog.id")
    @Mapping(target = "epicId", source = "epic.id")
    UserStoryDTO toDto(UserStory entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "epic", ignore = true)
    void updateEntityFromDto(UserStoryCreationDTO dto, @MappingTarget UserStory entity);
}
