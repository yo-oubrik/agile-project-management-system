package ma.ensa.apms.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.ensa.apms.dto.UserStoryCreationDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceImplTest {

        @Mock
        private UserStoryRepository userStoryRepository;

        @Mock
        private UserStoryMapper userStoryMapper;

        @Mock
        private ProductBacklogRepository productBacklogRepository;

        @Mock 
        private EpicRepository epicRepository ;

        @InjectMocks
        private UserStoryServiceImpl userStoryService;

        private UserStory userStory;
        private UserStoryDTO userStoryDTO;
        private UserStoryCreationDTO creationDTO;
        private UserStoryCreationDTO updateDTO;
        private ProductBacklog productBacklog;
        private Epic epic ; 

        @BeforeEach
        void setUp() {
                productBacklog = ProductBacklog.builder()
                                .id(1L)
                                .name("Test Backlog")
                                .build();

                userStory = UserStory.builder()
                                .id(1L)
                                .name("Test Story")
                                .role("Test Role")
                                .feature("Test Feature")
                                .benefit("Test Benefit")
                                .priority(UserStoryPriority.HIGH)
                                .status(UserStoryStatus.TODO)
                                .productBacklog(productBacklog)
                                .build();

                userStoryDTO = UserStoryDTO.builder()
                                .id(1L)
                                .name("Test Story")
                                .role("Test Role")
                                .feature("Test Feature")
                                .benefit("Test Benefit")
                                .priority("HIGH")
                                .status("TODO")
                                .productBacklogId(1L)
                                .build();

                creationDTO = UserStoryCreationDTO.builder()
                                .name("Test Story")
                                .role("Test Role")
                                .feature("Test Feature")
                                .benefit("Test Benefit")
                                .priority("HIGH")
                                .status("TODO")
                                .productBacklogId(1L)
                                .build();

                updateDTO = UserStoryCreationDTO.builder()
                                .name("Updated Story")
                                .role("Updated Role")
                                .feature("Updated Feature")
                                .benefit("Updated Benefit")
                                .priority("MEDIUM")
                                .status("IN_PROGRESS")
                                .productBacklogId(1L)
                                .build();

                epic = Epic.builder()
                                .id(1L)
                                .name("epic Name")
                                .description("epic Description")
                                .priority(5)
                                .status("TODO")
                                .build();

        }

        @Test
        void create_ShouldReturnSavedUserStory() {
                // Arrange
                when(userStoryMapper.toEntity(any(UserStoryCreationDTO.class)))
                                .thenReturn(userStory);
                when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
                when(userStoryRepository.save(any())).thenReturn(userStory);
                when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);

                // Act
                UserStoryDTO result = userStoryService.create(creationDTO);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(1L);
                verify(userStoryRepository).save(any());
        }

        @Test
        void create_ShouldThrowResourceNotFoundException_WhenProductBacklogNotFound() {
                // Arrange
                when(userStoryMapper.toEntity(any(UserStoryCreationDTO.class)))
                                .thenReturn(userStory);
                when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.create(creationDTO))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Product backlog not found");
        }

        
        @Test
        void update_ShouldReturnUpdatedUserStory() {
                // Arrange
                UserStory updatedEntity = UserStory.builder()
                                .id(1L)
                                .name("Updated Story")
                                .role("Updated Role")
                                .feature("Updated Feature")
                                .benefit("Updated Benefit")
                                .priority(UserStoryPriority.MEDIUM)
                                .status(UserStoryStatus.IN_PROGRESS)
                                .productBacklog(productBacklog)
                                .build();

                UserStoryDTO updatedDTO = UserStoryDTO.builder()
                                .id(1L)
                                .name("Updated Story")
                                .role("Updated Role")
                                .feature("Updated Feature")
                                .benefit("Updated Benefit")
                                .priority("MEDIUM")
                                .status("IN_PROGRESS")
                                .productBacklogId(1L)
                                .build();

                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
                when(userStoryRepository.save(any())).thenReturn(updatedEntity);
                when(userStoryMapper.toDto(updatedEntity)).thenReturn(updatedDTO);

                // Act
                UserStoryDTO result = userStoryService.updateUserStory(1L, updateDTO);
                
                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getName()).isEqualTo("Updated Story");
                verify(userStoryRepository).save(any());
        }

        @Test
        void update_ShouldThrowIllegalArgumentException_WhenIdIsNull() {
                // Arrange
                Long id = null;

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.updateUserStory(id, updateDTO))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("User story ID is required");
        }

        @Test
        void update_ShouldThrowResourceNotFoundException_WhenUserStoryNotFound() {
                // Arrange
                when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.updateUserStory(1L, updateDTO))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("User story not found");
        }

        @Test
        void update_ShouldThrowResourceNotFoundException_WhenProductBacklogNotFound() {
                // Arrange
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.updateUserStory(1L, updateDTO))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Product backlog not found");
        }

        @Test
        void changeStatus_ShouldUpdateStatus_WhenAllAcceptanceCriteriaMet() {
                // Arrange
                userStory.setAcceptanceCriterias(List.of(
                        AcceptanceCriteria.builder().met(true).build(),
                        AcceptanceCriteria.builder().met(true).build()
                        ));
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(userStoryRepository.save(any())).thenReturn(userStory);
                when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);

                // Act
                UserStoryDTO result = userStoryService.changeStatus(1L, UserStoryStatus.DONE);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getStatus()).isEqualTo("DONE");
                verify(userStoryRepository).save(any());
        }

        @Test
        void changeStatus_ShouldThrowBusinessException_WhenAcceptanceCriteriaNotMet() {
                // Arrange
                userStory.setAcceptanceCriterias(List.of(
                        AcceptanceCriteria.builder().met(true).build(),
                        AcceptanceCriteria.builder().met(false).build()
                ));
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.changeStatus(1L, UserStoryStatus.DONE))
                .isInstanceOf(BusinessException.class)
                .hasMessage("All acceptance criteria must be met to mark as DONE.");
        }

        @Test
        void changeStatus_ShouldThrowResourceNotFoundException_WhenUserStoryNotFound() {
                // Arrange
                when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.changeStatus(1L, UserStoryStatus.DONE))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("User story not found");
        }

        @Test
        void linkToEpic_ShouldLinkSuccessfully_WhenValidInputs() {
                // Arrange
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
                when(userStoryRepository.save(any())).thenReturn(userStory);
                when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);
                
                // Act
                UserStoryDTO result = userStoryService.linkToEpic(1L, 1L);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(1L);
                verify(userStoryRepository).save(any());
        }

        @Test
        void linkToEpic_ShouldThrowResourceNotFoundException_WhenUserStoryNotFound() {
                // Arrange
                when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.linkToEpic(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User Story not found");
        }

        @Test
        void linkToEpic_ShouldThrowResourceNotFoundException_WhenEpicNotFound() {
                // Arrange
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(epicRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.linkToEpic(1L, 1L))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Epic not found");
        }

        @Test
        void linkToEpic_ShouldThrowBusinessException_WhenUserStoryStatusNotTODO() {
                // Arrange
                userStory.setStatus(UserStoryStatus.IN_PROGRESS);
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
                
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.linkToEpic(1L, 1L))
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Cannot link an epic to a user story with status higher than TODO");
        }

        @Test 
        void getUserStoriesByStatusAndProductBacklogId_ShouldReturnListOfUserStories() {
                // Arrange
                when(userStoryRepository.findByStatusAndProductBacklogId(UserStoryStatus.TODO, 1L))
                                .thenReturn(List.of(userStory));
                when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);
        
                // Act
                List<UserStoryDTO> result = userStoryService.getUserStoriesByStatusAndProductBacklogId(UserStoryStatus.TODO, 1L);
        
                // Assert
                assertThat(result).isNotEmpty();
                assertThat(result.get(0).getId()).isEqualTo(1L);
        }

        @Test 
        void getUserStoriesByStatusAndProductBacklogId_shouldthrowIllegalArgumentException_WhenStatusIsNull() {
                // Arrange
                UserStoryStatus statut = null;
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.getUserStoriesByStatusAndProductBacklogId(statut, 1L))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Status is required");
        }

        @Test
        void getUserStoriesByStatusAndProductBacklogId_shouldthrowIllegalArgumentException_WhenProductBacklogIdIsNull() {
                // Arrange
                Long productBacklogId = null;
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.getUserStoriesByStatusAndProductBacklogId(UserStoryStatus.TODO, productBacklogId))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Product Backlog ID is required");
        }

        @Test
        void getUserStoriesByStatusAndProductBacklogId_ShouldThrowResourceNotFoundException_WhenProductBacklogNotFound() {
                // Arrange
                when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.getUserStoriesByStatusAndProductBacklogId(UserStoryStatus.TODO, 1L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Product Backlog not found");
        }

        @Test
        void getUserStoriesByEpicId_ShouldReturnListOfUserStories() {
                // Arrange
                when(userStoryRepository.findByEpicId(1L)).thenReturn(List.of(userStory));
                when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);
        
                // Act
                List<UserStoryDTO> result = userStoryService.getUserStoriesByEpicId(1L);
        
                // Assert
                assertThat(result).isNotEmpty();
                assertThat(result.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void getUserStoriesByEpicId_shouldthrowIllegalArgumentException_WhenEpicIdIsNull() {
                // Arrange
                Long epicId = null;
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.getUserStoriesByEpicId(epicId))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Epic ID is required");
        }

        @Test 
        void getUserStoriesByEpicId_ShouldThrowResourceNotFoundException_WhenEpicNotFound() {
                // Arrange
                when(epicRepository.findById(1L)).thenReturn(Optional.empty());
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.getUserStoriesByEpicId(1L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Epic not found");
        }

        @Test 
        void getUserStoriesBySprintId_ShouldReturnListOfUserStories() {
                // Arrange
                when(userStoryRepository.findBySprintId(1L)).thenReturn(List.of(userStory));
                when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);
        
                // Act
                List<UserStoryDTO> result = userStoryService.getUserStoriesBySprintId(1L);
        
                // Assert
                assertThat(result).isNotEmpty();
                assertThat(result.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void getUserStoriesBySprintId_shouldthrowIllegalArgumentException_WhenSprintIdIsNull() {
                // Arrange
                Long sprintId = null;
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.getUserStoriesBySprintId(sprintId))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Sprint ID is required");
        }

        @Test
        void getUserStoriesBySprintId_shouldthrowResourceNotFoundException_WhenSprintNotFound() {
                // Arrange
                // when(sprintRepository.findById(1L)).thenReturn(Optional.empty());
        
                // // Act & Assert
                // assertThatThrownBy(() -> userStoryService.getUserStoriesBySprintId(1L))
                //                 .isInstanceOf(ResourceNotFoundException.class)
                //                 .hasMessage("Sprint not found");
        }
        
        @Test
        void delete_ShouldDeleteSuccessfully_WhenExists() {
                // Arrange
                when(userStoryRepository.existsById(1L)).thenReturn(true);
        
                // Act
                userStoryService.delete(1L);
        
                // Assert
                verify(userStoryRepository).deleteById(1L);
        }
        
        @Test
        void delete_ShouldThrowResourceNotFoundException_WhenNotFound() {
                // Arrange
                when(userStoryRepository.existsById(999L)).thenReturn(false);
        
                // Act & Assert
                assertThatThrownBy(() -> userStoryService.delete(999L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("User story not found");
        }

        @Test
        void delete_ShouldThrowBusinessException_WhenStatusNotTODO() {
                // Arrange
                userStory.setStatus(UserStoryStatus.IN_PROGRESS);
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

                // Act & Assert
                assertThatThrownBy(() -> userStoryService.delete(1L))
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Only stories in TODO state can be deleted.");
        }
}