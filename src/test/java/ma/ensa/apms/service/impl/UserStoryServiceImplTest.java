// package ma.ensa.apms.service.impl;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import ma.ensa.apms.dto.UserStoryCreationDTO;
// import ma.ensa.apms.dto.UserStoryDTO;
// import ma.ensa.apms.exception.EmptyResourcesException;
// import ma.ensa.apms.exception.ResourceNotFoundException;
// import ma.ensa.apms.mapper.UserStoryMapper;
// import ma.ensa.apms.modal.ProductBacklog;
// import ma.ensa.apms.modal.UserStory;
// import ma.ensa.apms.modal.enums.UserStoryPriority;
// import ma.ensa.apms.modal.enums.UserStoryStatus;
// import ma.ensa.apms.repository.ProductBacklogRepository;
// import ma.ensa.apms.repository.UserStoryRepository;

// @ExtendWith(MockitoExtension.class)
// class UserStoryServiceImplTest {

//         @Mock
//         private UserStoryRepository userStoryRepository;

//         @Mock
//         private UserStoryMapper userStoryMapper;

//         @Mock
//         private ProductBacklogRepository productBacklogRepository;

//         @InjectMocks
//         private UserStoryServiceImpl userStoryService;

//         private UserStory userStory;
//         private UserStoryDTO userStoryDTO;
//         private UserStoryCreationDTO creationDTO;
//         private UserStoryCreationDTO updateDTO;
//         private ProductBacklog productBacklog;

//         @BeforeEach
//         void setUp() {
//                 productBacklog = ProductBacklog.builder()
//                                 .id(1L)
//                                 .name("Test Backlog")
//                                 .build();

//                 userStory = UserStory.builder()
//                                 .id(1L)
//                                 .name("Test Story")
//                                 .role("Test Role")
//                                 .feature("Test Feature")
//                                 .benefit("Test Benefit")
//                                 .priority(UserStoryPriority.HIGH)
//                                 .status(UserStoryStatus.TODO)
//                                 .productBacklog(productBacklog)
//                                 .build();

//                 userStoryDTO = UserStoryDTO.builder()
//                                 .id(1L)
//                                 .name("Test Story")
//                                 .role("Test Role")
//                                 .feature("Test Feature")
//                                 .benefit("Test Benefit")
//                                 .priority("HIGH")
//                                 .status("TODO")
//                                 .productBacklogId(1L)
//                                 .build();

//                 creationDTO = UserStoryCreationDTO.builder()
//                                 .name("Test Story")
//                                 .role("Test Role")
//                                 .feature("Test Feature")
//                                 .benefit("Test Benefit")
//                                 .priority("HIGH")
//                                 .status("TODO")
//                                 .productBacklogId(1L)
//                                 .build();

//                 updateDTO = UserStoryCreationDTO.builder()
//                                 .name("Updated Story")
//                                 .role("Updated Role")
//                                 .feature("Updated Feature")
//                                 .benefit("Updated Benefit")
//                                 .priority("MEDIUM")
//                                 .status("IN_PROGRESS")
//                                 .productBacklogId(1L)
//                                 .build();
//         }

//         @Test
//         void create_ShouldReturnSavedUserStory() {
//                 // Arrange
//                 when(userStoryMapper.toEntity(any(UserStoryCreationDTO.class)))
//                                 .thenReturn(userStory);
//                 when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
//                 when(userStoryRepository.save(any())).thenReturn(userStory);
//                 when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);

//                 // Act
//                 UserStoryDTO result = userStoryService.create(creationDTO);

//                 // Assert
//                 assertThat(result).isNotNull();
//                 assertThat(result.getId()).isEqualTo(1L);
//                 verify(userStoryRepository).save(any());
//         }

//         @Test
//         void create_ShouldThrowResourceNotFoundException_WhenProductBacklogNotFound() {
//                 // Arrange
//                 when(userStoryMapper.toEntity(any(UserStoryCreationDTO.class)))
//                                 .thenReturn(userStory);
//                 when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

//                 // Act & Assert
//                 assertThatThrownBy(() -> userStoryService.create(creationDTO))
//                                 .isInstanceOf(ResourceNotFoundException.class)
//                                 .hasMessage("Product backlog not found");
//         }

//         @Test
//         void findById_ShouldReturnUserStory_WhenExists() {
//                 // Arrange
//                 when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
//                 when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

//                 // Act
//                 UserStoryDTO result = userStoryService.findById(1L);

//                 // Assert
//                 assertThat(result).isNotNull();
//                 assertThat(result.getId()).isEqualTo(1L);
//         }

//         @Test
//         void findById_ShouldThrowResourceNotFoundException_WhenNotFound() {
//                 // Arrange
//                 when(userStoryRepository.findById(999L)).thenReturn(Optional.empty());

//                 // Act & Assert
//                 assertThatThrownBy(() -> userStoryService.findById(999L))
//                                 .isInstanceOf(ResourceNotFoundException.class)
//                                 .hasMessage("User story not found");
//         }

//         @Test
//         void delete_ShouldDeleteSuccessfully_WhenExists() {
//                 // Arrange
//                 when(userStoryRepository.existsById(1L)).thenReturn(true);

//                 // Act
//                 userStoryService.delete(1L);

//                 // Assert
//                 verify(userStoryRepository).deleteById(1L);
//         }

//         @Test
//         void delete_ShouldThrowResourceNotFoundException_WhenNotFound() {
//                 // Arrange
//                 when(userStoryRepository.existsById(999L)).thenReturn(false);

//                 // Act & Assert
//                 assertThatThrownBy(() -> userStoryService.delete(999L))
//                                 .isInstanceOf(ResourceNotFoundException.class)
//                                 .hasMessage("User story not found");
//         }

//         @Test
//         void findAll_ShouldThrowEmptyResourcesException_WhenNoUserStoryExists() {
//                 // Arrange
//                 when(userStoryRepository.findAll()).thenReturn(List.of());

//                 // Act & Assert
//                 assertThatThrownBy(() -> userStoryService.findAll())
//                                 .isInstanceOf(EmptyResourcesException.class)
//                                 .hasMessage("No user stories found");
//         }

//         @Test
//         void update_ShouldReturnUpdatedUserStory() {
//                 // Arrange
//                 UserStory updatedEntity = UserStory.builder()
//                                 .id(1L)
//                                 .name("Updated Story")
//                                 .role("Updated Role")
//                                 .feature("Updated Feature")
//                                 .benefit("Updated Benefit")
//                                 .priority(UserStoryPriority.MEDIUM)
//                                 .status(UserStoryStatus.IN_PROGRESS)
//                                 .productBacklog(productBacklog)
//                                 .build();

//                 UserStoryDTO updatedDTO = UserStoryDTO.builder()
//                                 .id(1L)
//                                 .name("Updated Story")
//                                 .role("Updated Role")
//                                 .feature("Updated Feature")
//                                 .benefit("Updated Benefit")
//                                 .priority("MEDIUM")
//                                 .status("IN_PROGRESS")
//                                 .productBacklogId(1L)
//                                 .build();

//                 when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
//                 when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
//                 when(userStoryRepository.save(any())).thenReturn(updatedEntity);
//                 when(userStoryMapper.toDto(updatedEntity)).thenReturn(updatedDTO);

//                 // Act
//                 UserStoryDTO result = userStoryService.update(1L, updateDTO);

//                 // Assert
//                 assertThat(result).isNotNull();
//                 assertThat(result.getId()).isEqualTo(1L);
//                 assertThat(result.getName()).isEqualTo("Updated Story");
//                 verify(userStoryRepository).save(any());
//         }

//         @Test
//         void update_ShouldThrowIllegalArgumentException_WhenIdIsNull() {
//                 // Arrange
//                 Long id = null;

//                 // Act & Assert
//                 assertThatThrownBy(() -> userStoryService.update(id, updateDTO))
//                                 .isInstanceOf(IllegalArgumentException.class)
//                                 .hasMessage("User story ID is required");
//         }

//         @Test
//         void update_ShouldThrowResourceNotFoundException_WhenUserStoryNotFound() {
//                 // Arrange
//                 when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

//                 // Act & Assert
//                 assertThatThrownBy(() -> userStoryService.update(1L, updateDTO))
//                                 .isInstanceOf(ResourceNotFoundException.class)
//                                 .hasMessage("User story not found");
//         }
// }
