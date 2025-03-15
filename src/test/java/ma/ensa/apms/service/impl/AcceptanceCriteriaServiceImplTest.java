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

import ma.ensa.apms.dto.AcceptanceCriteriaCreationDTO;
import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.exception.EmptyResourcesException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;
import ma.ensa.apms.repository.UserStoryRepository;

@ExtendWith(MockitoExtension.class)
class AcceptanceCriteriaServiceImplTest {

        @Mock
        private AcceptanceCriteriaRepository acceptanceCriteriaRepository;

        @Mock
        private AcceptanceCriteriaMapper acceptanceCriteriaMapper;

        @Mock
        private UserStoryRepository userStoryRepository;

        @InjectMocks
        private AcceptanceCriteriaServiceImpl acceptanceCriteriaService;

        private AcceptanceCriteria acceptanceCriteria;
        private AcceptanceCriteriaDTO acceptanceCriteriaDTO;
        private AcceptanceCriteriaCreationDTO creationDTO;
        private AcceptanceCriteriaCreationDTO updateDTO;
        private UserStory userStory;

        @BeforeEach
        void setUp() {
                userStory = UserStory.builder()
                                .id(1L)
                                .name("Test Story")
                                .build();

                acceptanceCriteria = AcceptanceCriteria.builder()
                                .id(1L)
                                .given("Given test condition")
                                .when("When test action")
                                .then("Then test result")
                                .userStory(userStory)
                                .build();

                acceptanceCriteriaDTO = AcceptanceCriteriaDTO.builder()
                                .id(1L)
                                .given("Given test condition")
                                .when("When test action")
                                .then("Then test result")
                                .build();

                creationDTO = AcceptanceCriteriaCreationDTO.builder()
                                .given("Given test condition")
                                .when("When test action")
                                .then("Then test result")
                                .userStoryId(1L)
                                .build();

                updateDTO = AcceptanceCriteriaCreationDTO.builder()
                                .given("Updated given")
                                .when("Updated when")
                                .then("Updated then")
                                .userStoryId(1L)
                                .build();
        }

        @Test
        void create_ShouldReturnSavedAcceptanceCriteria() {
                // Arrange
                when(acceptanceCriteriaMapper.toEntity(any(AcceptanceCriteriaCreationDTO.class)))
                                .thenReturn(acceptanceCriteria);
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(acceptanceCriteriaRepository.save(any())).thenReturn(acceptanceCriteria);
                when(acceptanceCriteriaMapper.toDto(any())).thenReturn(acceptanceCriteriaDTO);

                // Act
                AcceptanceCriteriaDTO result = acceptanceCriteriaService.create(creationDTO);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(1L);
                verify(acceptanceCriteriaRepository).save(any());
        }

        @Test
        void create_ShouldThrowResourceNotFoundException_WhenUserStoryNotFound() {
                // Arrange
                when(acceptanceCriteriaMapper.toEntity(any(AcceptanceCriteriaCreationDTO.class)))
                                .thenReturn(acceptanceCriteria);
                when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.create(creationDTO))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("User story not found");
        }

        @Test
        void findById_ShouldReturnAcceptanceCriteria_WhenExists() {
                // Arrange
                when(acceptanceCriteriaRepository.findById(1L)).thenReturn(Optional.of(acceptanceCriteria));
                when(acceptanceCriteriaMapper.toDto(acceptanceCriteria)).thenReturn(acceptanceCriteriaDTO);

                // Act
                AcceptanceCriteriaDTO result = acceptanceCriteriaService.findById(1L);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        void findById_ShouldThrowResourceNotFoundException_WhenNotFound() {
                // Arrange
                when(acceptanceCriteriaRepository.findById(999L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.findById(999L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Acceptance Criteria not found");
        }

        @Test
        void delete_ShouldDeleteSuccessfully_WhenExists() {
                // Arrange
                when(acceptanceCriteriaRepository.existsById(1L)).thenReturn(true);

                // Act
                acceptanceCriteriaService.delete(1L);

                // Assert
                verify(acceptanceCriteriaRepository).deleteById(1L);
        }

        @Test
        void delete_ShouldThrowResourceNotFoundException_WhenNotFound() {
                // Arrange
                when(acceptanceCriteriaRepository.existsById(999L)).thenReturn(false);

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.delete(999L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Acceptance Criteria not found");
        }

        @Test
        void findAll_ShouldThrowEmptyResourcesException_WhenNoAcceptanceCriteriaExists() {
                // Arrange
                when(acceptanceCriteriaRepository.findAll()).thenReturn(List.of());

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.findAll())
                                .isInstanceOf(EmptyResourcesException.class)
                                .hasMessage("No acceptance criteria found");
        }

        @Test
        void update_ShouldReturnUpdatedAcceptanceCriteria() {
                // Arrange
                AcceptanceCriteria updatedEntity = AcceptanceCriteria.builder()
                                .id(1L)
                                .given("Updated given")
                                .when("Updated when")
                                .then("Updated then")
                                .userStory(userStory)
                                .build();

                AcceptanceCriteriaDTO updatedDTO = AcceptanceCriteriaDTO.builder()
                                .id(1L)
                                .given("Updated given")
                                .when("Updated when")
                                .then("Updated then")
                                .build();

                when(acceptanceCriteriaRepository.findById(1L)).thenReturn(Optional.of(acceptanceCriteria));
                when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
                when(acceptanceCriteriaRepository.save(any())).thenReturn(updatedEntity);
                when(acceptanceCriteriaMapper.toDto(updatedEntity)).thenReturn(updatedDTO);

                // Act
                AcceptanceCriteriaDTO result = acceptanceCriteriaService.update(1L, updateDTO);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getGiven()).isEqualTo("Updated given");
                assertThat(result.getWhen()).isEqualTo("Updated when");
                assertThat(result.getThen()).isEqualTo("Updated then");
                verify(acceptanceCriteriaRepository).save(any());
        }

        @Test
        void update_ShouldThrowIllegalArgumentException_WhenIdIsNull() {
                // Arrange
                Long id = null;

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.update(id, updateDTO))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Acceptance Criteria ID is required");
        }

        @Test
        void update_ShouldThrowResourceNotFoundException_WhenAcceptanceCriteriaNotFound() {
                // Arrange
                when(acceptanceCriteriaRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.update(1L, updateDTO))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("Acceptance Criteria not found");
        }

        @Test
        void update_ShouldThrowResourceNotFoundException_WhenUserStoryNotFound() {
                // Arrange
                when(acceptanceCriteriaRepository.findById(1L)).thenReturn(Optional.of(acceptanceCriteria));
                when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                assertThatThrownBy(() -> acceptanceCriteriaService.update(1L, updateDTO))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage("User story not found");
        }
}