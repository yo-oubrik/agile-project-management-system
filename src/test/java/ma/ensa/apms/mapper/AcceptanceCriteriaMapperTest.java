package ma.ensa.apms.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ma.ensa.apms.dto.AcceptanceCriteriaCreationDTO;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;

@SpringBootTest(classes = { AcceptanceCriteriaMapperImpl.class })
class AcceptanceCriteriaMapperTest {

        @Autowired
        private AcceptanceCriteriaMapper mapper;

        @Test
        void toEntity_ShouldMapDtoToEntity() {
                // Arrange
                AcceptanceCriteriaCreationDTO dto = AcceptanceCriteriaCreationDTO.builder()
                                .given("Given precondition")
                                .when("When action")
                                .then("Then result")
                                .userStoryId(1L)
                                .build();

                AcceptanceCriteria entity = mapper.toEntity(dto);

                // Assert
                assertThat(entity).isNotNull();
                assertThat(entity.getId()).isNull(); // ID should be ignored
                assertThat(entity.getGiven()).isEqualTo("Given precondition");
                assertThat(entity.getWhen()).isEqualTo("When action");
                assertThat(entity.getThen()).isEqualTo("Then result");
                assertThat(entity.getUserStory()).isNull(); // UserStory should be ignored
        }

        @Test
        void toDto_ShouldMapEntityToDto() {
                // Arrange
                UserStory userStory = UserStory.builder()
                                .id(1L)
                                .name("Test Story")
                                .build();

                AcceptanceCriteria entity = AcceptanceCriteria.builder()
                                .id(1L)
                                .given("Given precondition")
                                .when("When action")
                                .then("Then result")
                                .userStory(userStory)
                                .build();

                // Act
                AcceptanceCriteriaDTO dto = mapper.toDto(entity);

                // Assert
                assertThat(dto).isNotNull();
                assertThat(dto.getId()).isEqualTo(1L);
                assertThat(dto.getGiven()).isEqualTo("Given precondition");
                assertThat(dto.getWhen()).isEqualTo("When action");
                assertThat(dto.getThen()).isEqualTo("Then result");
                // No userStory field in DTO, so no assertion needed
        }

        @Test
        void updateEntityFromDto_ShouldUpdateEntityFields() {
                // Arrange
                UserStory userStory = UserStory.builder()
                                .id(1L)
                                .name("Original Story")
                                .build();

                AcceptanceCriteria entity = AcceptanceCriteria.builder()
                                .id(1L)
                                .given("Original given")
                                .when("Original when")
                                .then("Original then")
                                .userStory(userStory)
                                .build();

                AcceptanceCriteriaCreationDTO dto = AcceptanceCriteriaCreationDTO.builder()
                                .given("Updated given")
                                .when("Updated when")
                                .then("Updated then")
                                .userStoryId(2L) // Different userStoryId
                                .build();

                // Act
                mapper.updateEntityFromDto(dto, entity);

                // Assert
                assertThat(entity.getId()).isEqualTo(1L); // ID should not change
                assertThat(entity.getGiven()).isEqualTo("Updated given");
                assertThat(entity.getWhen()).isEqualTo("Updated when");
                assertThat(entity.getThen()).isEqualTo("Updated then");
                assertThat(entity.getUserStory()).isEqualTo(userStory); // UserStory should not change
                assertThat(entity.getUserStory().getId()).isEqualTo(1L); // UserStory ID should not change
        }
}