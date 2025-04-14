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

import ma.ensa.apms.dto.ProductBacklogRequest;
import ma.ensa.apms.dto.ProductBacklogResponse;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceImplTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @InjectMocks
    private ProductBacklogServiceImpl productBacklogService;

    private ProductBacklog productBacklog;
    private ProductBacklogRequest productBacklogRequest;
    private ProductBacklogResponse productBacklogResponse;
    private UserStory userStory;
    private UserStoryDTO userStoryDTO;

    @BeforeEach
    void setUp() {
        productBacklog = ProductBacklog.builder()
                .id(1L)
                .name("Test Backlog")
                .build();

        productBacklogRequest = ProductBacklogRequest.builder()
                .name("Test Backlog")
                .build();

        productBacklogResponse = ProductBacklogResponse.builder()
                .id(1L)
                .name("Test Backlog")
                .build();

        userStory = UserStory.builder()
                .id(1L)
                .name("Test Story")
                .build();

        userStoryDTO = UserStoryDTO.builder()
                .id(1L)
                .name("Test Story")
                .build();
    }

    @Test
    void create_ShouldReturnSavedProductBacklog() {
        // Arrange
        when(productBacklogMapper.toEntity(any(ProductBacklogRequest.class))).thenReturn(productBacklog);
        when(productBacklogRepository.save(any())).thenReturn(productBacklog);
        when(productBacklogMapper.toResponse(any())).thenReturn(productBacklogResponse);

        // Act
        ProductBacklogResponse result = productBacklogService.create(productBacklogRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(productBacklogRepository).save(any());
    }

    @Test
    void getBacklogUserStoriesSorted_ShouldReturnSortedUserStories_WhenProductBacklogExists() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(userStoryRepository.findByProductBacklogIdOrderByPriorityAsc(1L)).thenReturn(List.of(userStory));
        when(userStoryMapper.toDto(any())).thenReturn(userStoryDTO);

        // Act
        List<UserStoryDTO> result = productBacklogService.getBacklogUserStoriesSorted(1L);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getBacklogUserStoriesSorted_ShouldThrowResourceNotFoundException_WhenProductBacklogNotFound() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productBacklogService.getBacklogUserStoriesSorted(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product backlog not found");
    }

    @Test
    void getProductBacklogById_ShouldReturnProductBacklog_WhenExists() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(productBacklogMapper.toResponse(any())).thenReturn(productBacklogResponse);

        // Act
        ProductBacklogResponse result = productBacklogService.getProductBacklogById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getProductBacklogById_ShouldThrowResourceNotFoundException_WhenNotFound() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productBacklogService.getProductBacklogById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("backlog not found");
    }

    @Test
    void deleteProductBacklog_ShouldDeleteSuccessfully_WhenExists() {
        // Act
        productBacklogService.deleteProductBacklog(1L);

        // Assert
        verify(productBacklogRepository).deleteById(1L);
    }
}