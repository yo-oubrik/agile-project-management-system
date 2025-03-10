package ma.ensa.apms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Epic;

@Mapper(componentModel = "spring", uses = { AcceptanceCriteriaMapper.class })
public interface UserStoryMapper {

    @Mapping(target = "productBacklog", source = "productBacklogId", qualifiedByName = "toProductBacklog")
    @Mapping(target = "epic", source = "epicId", qualifiedByName = "toEpic")
    UserStory toEntity(UserStoryDTO dto);

    @Mapping(target = "productBacklogId", source = "productBacklog.id")
    @Mapping(target = "epicId", source = "epic.id")
    UserStoryDTO toDto(UserStory entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productBacklog", source = "productBacklogId", qualifiedByName = "toProductBacklog")
    @Mapping(target = "epic", source = "epicId", qualifiedByName = "toEpic")
    void updateEntityFromDto(UserStoryDTO dto, @MappingTarget UserStory entity);

    List<UserStoryDTO> toDtoList(List<UserStory> entities);

    @Named("toProductBacklog")
    default ProductBacklog toProductBacklog(Long id) {
        if (id == null)
            return null;
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setId(id);
        return productBacklog;
    }

    @Named("toEpic")
    default Epic toEpic(Long id) {
        if (id == null)
            return null;
        Epic epic = new Epic();
        epic.setId(id);
        return epic;
    }
}