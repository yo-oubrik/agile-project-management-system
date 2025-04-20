package ma.ensa.apms.mapper;

import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.modal.Epic;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EpicMapper {


//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "userStories", ignore = true)
    Epic toEntity(EpicCreationDTO dto);

    EpicDTO toDto(Epic entity);


//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "userStories", ignore = true)
    void updateEntityFromDto(EpicCreationDTO dto, @MappingTarget Epic entity);
}