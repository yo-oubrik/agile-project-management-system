package ma.ensa.apms.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.modal.Epic;

@SpringBootTest(classes = { EpicMapperImpl.class })
class EpicMapperTest {

    @Autowired
    private EpicMapper mapper;

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        // Arrange
        EpicCreationDTO dto = EpicCreationDTO.builder()
                .name("Epic 1")
                .description("Description for Epic 1")
                .build();

        // Act
        Epic entity = mapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull(); // ID should be ignored
        assertThat(entity.getName()).isEqualTo("Epic 1");
        assertThat(entity.getDescription()).isEqualTo("Description for Epic 1");
    }

    @Test
    void toDto_ShouldMapEntityToDto() {
        // Arrange
        Epic entity = Epic.builder()
                .id(1L)
                .name("Epic 1")
                .description("Description for Epic 1")
                .build();

        // Act
        EpicDTO dto = mapper.toDto(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Epic 1");
        assertThat(dto.getDescription()).isEqualTo("Description for Epic 1");
    }

    @Test
    void updateEntityFromDto_ShouldUpdateEntityFields() {
        // Arrange
        Epic entity = Epic.builder()
                .id(1L)
                .name("Original Epic")
                .description("Original Description")
                .build();

        EpicCreationDTO dto = EpicCreationDTO.builder()
                .name("Updated Epic")
                .description("Updated Description")
                .build();

        // Act
        mapper.updateEntityFromDto(dto, entity);

        // Assert
        assertThat(entity.getId()).isEqualTo(1L); // ID should not change
        assertThat(entity.getName()).isEqualTo("Updated Epic");
        assertThat(entity.getDescription()).isEqualTo("Updated Description");
    }
}