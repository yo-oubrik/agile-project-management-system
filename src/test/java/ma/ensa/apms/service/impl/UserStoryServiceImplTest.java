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

import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.repository.UserStoryRepository;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceImplTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private UserStoryServiceImpl userStoryService;

    private UserStory userStory;
    private UserStoryDTO userStoryDTO;

    @BeforeEach
    void setUp() {
        userStory = UserStory.builder()
                .id(1L)
                .name("Test Story")
                .role("As a tester")
                .feature("I want to test the service")
                .benefit("So that I can ensure it works")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .build();

        userStoryDTO = UserStoryDTO.builder()
                .id(1L)
                .name("Test Story")
                .role("As a tester")
                .feature("I want to test the service")
                .benefit("So that I can ensure it works")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .build();
    }

    @Test
    void save_ShouldReturnSavedUserStoryDTO() {
        // Arrange
        when(userStoryMapper.toEntity(userStoryDTO)).thenReturn(userStory);
        when(userStoryRepository.save(userStory)).thenReturn(userStory);
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.save(userStoryDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(userStoryDTO.getName());
        verify(userStoryRepository).save(any(UserStory.class));
    }

    @Test
    void findById_ShouldReturnUserStoryDTO_WhenUserStoryExists() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenUserStoryDoesNotExist() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userStoryService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User Story not found");
    }

    @Test
    void findAll_ShouldReturnAllUserStories() {
        // Arrange
        List<UserStory> userStories = List.of(userStory);
        when(userStoryRepository.findAll()).thenReturn(userStories);
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        // Act
        List<UserStoryDTO> result = userStoryService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(userStoryDTO.getName());
    }

    @Test
    void update_ShouldReturnUpdatedUserStoryDTO_WhenUserStoryExists() {
        // Arrange
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(userStory)).thenReturn(userStory);
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        // Act
        UserStoryDTO result = userStoryService.update(1L, userStoryDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(userStoryMapper).updateEntityFromDto(userStoryDTO, userStory);
    }

    @Test
    // We suppose that userStoryRepository methods are working correctly
    void delete_ShouldDeleteUserStory_WhenUserStoryExists() {
        // Arrange
        when(userStoryRepository.existsById(1L)).thenReturn(true);

        // Act
        userStoryService.delete(1L);

        // Assert
        verify(userStoryRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenUserStoryDoesNotExist() {
        // Arrange
        when(userStoryRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userStoryService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User Story not found");
    }
}