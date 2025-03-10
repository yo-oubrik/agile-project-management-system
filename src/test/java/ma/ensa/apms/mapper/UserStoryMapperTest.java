package ma.ensa.apms.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@SpringBootTest(classes = { UserStoryMapperImpl.class, AcceptanceCriteriaMapperImpl.class })
class UserStoryMapperTest {

    @Autowired
    private UserStoryMapper mapper;

    @Test
    void toEntity_ShouldMapAllFields() {
        // Arrange
        AcceptanceCriteriaDTO acceptanceCriteriaDTO = AcceptanceCriteriaDTO.builder()
                .id(1L)
                .given("Given condition")
                .when("When action")
                .then("Then result")
                .build();

        UserStoryDTO dto = UserStoryDTO.builder()
                .id(1L)
                .name("Test Story")
                .role("As a tester")
                .feature("I want to test mapping")
                .benefit("So that I can ensure it works")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .productBacklogId(1L)
                .epicId(1L)
                .acceptanceCriterias(List.of(acceptanceCriteriaDTO))
                .build();

        // Act
        UserStory entity = mapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getRole()).isEqualTo(dto.getRole());
        assertThat(entity.getFeature()).isEqualTo(dto.getFeature());
        assertThat(entity.getBenefit()).isEqualTo(dto.getBenefit());
        assertThat(entity.getPriority()).isEqualTo(dto.getPriority());
        assertThat(entity.getStatus()).isEqualTo(dto.getStatus());
        assertThat(entity.getProductBacklog().getId()).isEqualTo(dto.getProductBacklogId());
        assertThat(entity.getEpic().getId()).isEqualTo(dto.getEpicId());
        assertThat(entity.getAcceptanceCriterias()).hasSize(1);
    }

    @Test
    void toDto_ShouldMapAllFields() {
        // Arrange
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setId(1L);

        Epic epic = new Epic();
        epic.setId(1L);

        AcceptanceCriteria acceptanceCriteria = AcceptanceCriteria.builder()
                .id(1L)
                .given("Given condition")
                .when("When action")
                .then("Then result")
                .build();

        UserStory entity = UserStory.builder()
                .id(1L)
                .name("Test Story")
                .role("As a tester")
                .feature("I want to test mapping")
                .benefit("So that I can ensure it works")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .productBacklog(productBacklog)
                .epic(epic)
                .acceptanceCriterias(List.of(acceptanceCriteria))
                .build();

        // Act
        UserStoryDTO dto = mapper.toDto(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getName()).isEqualTo(entity.getName());
        assertThat(dto.getRole()).isEqualTo(entity.getRole());
        assertThat(dto.getFeature()).isEqualTo(entity.getFeature());
        assertThat(dto.getBenefit()).isEqualTo(entity.getBenefit());
        assertThat(dto.getPriority()).isEqualTo(entity.getPriority());
        assertThat(dto.getStatus()).isEqualTo(entity.getStatus());
        assertThat(dto.getProductBacklogId()).isEqualTo(entity.getProductBacklog().getId());
        assertThat(dto.getEpicId()).isEqualTo(entity.getEpic().getId());
        assertThat(dto.getAcceptanceCriterias()).hasSize(1);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateAllFields() {
        // Arrange
        UserStory existingEntity = UserStory.builder()
                .id(1L)
                .name("Old Name")
                .role("Old Role")
                .build();

        UserStoryDTO dto = UserStoryDTO.builder()
                .name("New Name")
                .role("New Role")
                .feature("New Feature")
                .benefit("New Benefit")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .productBacklogId(1L)
                .epicId(1L)
                .build();

        // Act
        mapper.updateEntityFromDto(dto, existingEntity);

        // Assert
        assertThat(existingEntity.getId()).isEqualTo(1L); // Should not change
        assertThat(existingEntity.getName()).isEqualTo("New Name");
        assertThat(existingEntity.getRole()).isEqualTo("New Role");
        assertThat(existingEntity.getProductBacklog().getId()).isEqualTo(1L);
        assertThat(existingEntity.getEpic().getId()).isEqualTo(1L);
    }

    @Test
    void toDtoList_ShouldMapAllEntities() {
        // Arrange
        UserStory entity1 = UserStory.builder()
                .id(1L)
                .name("Story 1")
                .build();

        UserStory entity2 = UserStory.builder()
                .id(2L)
                .name("Story 2")
                .build();

        // Act
        List<UserStoryDTO> dtos = mapper.toDtoList(List.of(entity1, entity2));

        // Assert
        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(UserStoryDTO::getName)
                .containsExactly("Story 1", "Story 2");
    }
}