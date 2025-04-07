// package ma.ensa.apms.mapper;

// import static org.assertj.core.api.Assertions.assertThat;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import ma.ensa.apms.dto.UserStoryCreationDTO;
// import ma.ensa.apms.dto.UserStoryDTO;
// import ma.ensa.apms.modal.UserStory;
// import ma.ensa.apms.modal.enums.UserStoryPriority;
// import ma.ensa.apms.modal.enums.UserStoryStatus;

// @SpringBootTest(classes = { UserStoryMapperImpl.class })
// class UserStoryMapperTest {

//         @Autowired
//         private UserStoryMapper mapper;

//         @Test
//         void toEntity_ShouldMapDtoToEntity() {
//                 // Arrange
//                 UserStoryCreationDTO dto = UserStoryCreationDTO.builder()
//                                 .name("Test Story")
//                                 .role("Test Role")
//                                 .feature("Test Feature")
//                                 .benefit("Test Benefit")
//                                 .priority("HIGH")
//                                 .status("TODO")
//                                 .productBacklogId(1L)
//                                 .build();

//                 UserStory entity = mapper.toEntity(dto);

//                 // Assert
//                 assertThat(entity).isNotNull();
//                 assertThat(entity.getId()).isNull(); // ID should be ignored
//                 assertThat(entity.getName()).isEqualTo("Test Story");
//                 assertThat(entity.getRole()).isEqualTo("Test Role");
//                 assertThat(entity.getFeature()).isEqualTo("Test Feature");
//                 assertThat(entity.getBenefit()).isEqualTo("Test Benefit");
//                 assertThat(entity.getPriority()).isEqualTo(UserStoryPriority.HIGH);
//                 assertThat(entity.getStatus()).isEqualTo(UserStoryStatus.TODO);
//                 assertThat(entity.getProductBacklog()).isNull(); // ProductBacklog should be ignored
//         }

//         @Test
//         void toDto_ShouldMapEntityToDto() {
//                 // Arrange
//                 UserStory entity = UserStory.builder()
//                                 .id(1L)
//                                 .name("Test Story")
//                                 .role("Test Role")
//                                 .feature("Test Feature")
//                                 .benefit("Test Benefit")
//                                 .priority(UserStoryPriority.HIGH)
//                                 .status(UserStoryStatus.TODO)
//                                 .build();

//                 // Act
//                 UserStoryDTO dto = mapper.toDto(entity);

//                 // Assert
//                 assertThat(dto).isNotNull();
//                 assertThat(dto.getId()).isEqualTo(1L);
//                 assertThat(dto.getName()).isEqualTo("Test Story");
//                 assertThat(dto.getRole()).isEqualTo("Test Role");
//                 assertThat(dto.getFeature()).isEqualTo("Test Feature");
//                 assertThat(dto.getBenefit()).isEqualTo("Test Benefit");
//                 assertThat(dto.getPriority()).isEqualTo("HIGH");
//                 assertThat(dto.getStatus()).isEqualTo("TODO");
//         }

//         @Test
//         void updateEntityFromDto_ShouldUpdateEntityFields() {
//                 // Arrange
//                 UserStory entity = UserStory.builder()
//                                 .id(1L)
//                                 .name("Original Story")
//                                 .role("Original Role")
//                                 .feature("Original Feature")
//                                 .benefit("Original Benefit")
//                                 .priority(UserStoryPriority.HIGH)
//                                 .status(UserStoryStatus.TODO)
//                                 .build();

//                 UserStoryCreationDTO dto = UserStoryCreationDTO.builder()
//                                 .name("Updated Story")
//                                 .role("Updated Role")
//                                 .feature("Updated Feature")
//                                 .benefit("Updated Benefit")
//                                 .priority("MEDIUM")
//                                 .status("IN_PROGRESS")
//                                 .productBacklogId(1L)
//                                 .build();

//                 // Act
//                 mapper.updateEntityFromDto(dto, entity);

//                 // Assert
//                 assertThat(entity.getId()).isEqualTo(1L); // ID should not change
//                 assertThat(entity.getName()).isEqualTo("Updated Story");
//                 assertThat(entity.getRole()).isEqualTo("Updated Role");
//                 assertThat(entity.getFeature()).isEqualTo("Updated Feature");
//                 assertThat(entity.getBenefit()).isEqualTo("Updated Benefit");
//                 assertThat(entity.getPriority()).isEqualTo(UserStoryPriority.MEDIUM);
//                 assertThat(entity.getStatus()).isEqualTo(UserStoryStatus.IN_PROGRESS);
//                 assertThat(entity.getProductBacklog()).isNull(); // ProductBacklog should not change
//         }
// }
