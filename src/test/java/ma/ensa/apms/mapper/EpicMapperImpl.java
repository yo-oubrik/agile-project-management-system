package ma.ensa.apms.mapper;

import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.modal.Epic;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;


@Component
public class EpicMapperImpl implements EpicMapper {

    @Override
    public Epic toEntity(EpicCreationDTO dto) {
        if (dto == null) {
            return null;
        }

        Epic epic = new Epic();
        epic.setName(dto.getName());
        epic.setDescription(dto.getDescription());
        return epic;
    }

    @Override
    public EpicDTO toDto(Epic entity) {
        if (entity == null) {
            return null;
        }

        EpicDTO dto = new EpicDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    @Override
    public void updateEntityFromDto(EpicCreationDTO dto, Epic entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}