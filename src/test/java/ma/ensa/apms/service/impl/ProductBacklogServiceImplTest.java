package ma.ensa.apms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.ProjectMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

class ProductBacklogServiceImplTest {

        @InjectMocks
        private ProductBacklogServiceImpl productBacklogService;

        @Mock
        private ProductBacklogRepository productBacklogRepository;

        @Mock
        private UserStoryRepository userStoryRepository;

        @Mock
        private EpicRepository epicRepository;

        @Mock
        private ProductBacklogMapper productBacklogMapper;

        @Mock
        private UserStoryMapper userStoryMapper;

        @Mock
        private EpicMapper epicMapper;

        @Mock
        private ProjectMapper projectMapper;

        private UUID productBacklogId;
        private ProductBacklog productBacklog;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                productBacklogId = UUID.randomUUID();
                productBacklog = ProductBacklog.builder()
                                .id(productBacklogId)
                                .name("Test Backlog")
                                .build();
        }

        @Test
        void testCreateProductBacklog() {
                ProductBacklogRequest request = ProductBacklogRequest.builder().build();
                ProductBacklogResponse response = ProductBacklogResponse.builder()
                                .id(productBacklogId)
                                .name("Test Backlog")
                                .build();

                when(productBacklogMapper.toEntity(request)).thenReturn(productBacklog);
                when(productBacklogRepository.save(productBacklog)).thenReturn(productBacklog);
                when(productBacklogMapper.toResponse(productBacklog)).thenReturn(response);

                ProductBacklogResponse result = productBacklogService.create(request);

                assertNotNull(result);
                verify(productBacklogRepository).save(productBacklog);
        }

        @Test
        void testGetProductBacklogById() {
                ProductBacklogResponse response = ProductBacklogResponse.builder()
                                .id(productBacklogId)
                                .name("Test Backlog")
                                .build();

                when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));
                when(productBacklogMapper.toResponse(productBacklog)).thenReturn(response);

                ProductBacklogResponse result = productBacklogService.getProductBacklogById(productBacklogId);

                assertNotNull(result);
                verify(productBacklogRepository).findById(productBacklogId);
        }

        @Test
        void testGetProductBacklogById_NotFound() {
                when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> productBacklogService.getProductBacklogById(productBacklogId));
        }

        @Test
        void testDeleteProductBacklog() {
                when(productBacklogRepository.existsById(productBacklogId)).thenReturn(true);

                productBacklogService.deleteProductBacklog(productBacklogId);

                verify(productBacklogRepository).deleteById(productBacklogId);
        }

        @Test
        void testDeleteProductBacklog_NotFound() {
                when(productBacklogRepository.existsById(productBacklogId)).thenReturn(false);

                assertThrows(ResourceNotFoundException.class,
                                () -> productBacklogService.deleteProductBacklog(productBacklogId));
        }

        @Test
        void testGetAllProductBacklogs() {
                List<ProductBacklog> backlogs = List.of(productBacklog);
                List<ProductBacklogResponse> responses = List.of(ProductBacklogResponse.builder()
                                .id(productBacklogId)
                                .name("Test Backlog")
                                .build());

                when(productBacklogRepository.findAll()).thenReturn(backlogs);
                when(productBacklogMapper.toResponse(productBacklog)).thenReturn(responses.get(0));

                List<ProductBacklogResponse> result = productBacklogService.getAllProductBacklogs();

                assertEquals(1, result.size());
                verify(productBacklogRepository).findAll();
        }

        @Test
        void testAddEpicToProductBacklog() {
                // Arrange
                EpicRequest epicRequest = EpicRequest.builder()
                                .name("Test Epic")
                                .description("Test Description")
                                .build();

                Epic epic = Epic.builder()
                                .id(UUID.randomUUID())
                                .name("Test Epic")
                                .build();

                EpicResponse epicResponse = EpicResponse.builder()
                                .id(epic.getId())
                                .name(epic.getName())
                                .build();

                // Initialize the epics list in the product backlog as mutable
                productBacklog.setEpics(new ArrayList<>());

                when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));
                when(epicMapper.toEntity(epicRequest)).thenReturn(epic);
                when(epicRepository.save(epic)).thenReturn(epic);
                when(epicMapper.toDto(epic)).thenReturn(epicResponse);

                // Act
                EpicResponse result = productBacklogService.addEpicToProductBacklog(productBacklogId, epicRequest);

                // Assert
                assertNotNull(result);
                assertEquals(epicResponse.getId(), result.getId());
                assertEquals(epicResponse.getName(), result.getName());
                verify(epicRepository).save(epic);
                verify(productBacklogRepository).save(productBacklog);
        }

        @Test
        void testAddUserStoryToProductBacklog() {
                // Arrange
                UserStoryRequest userStoryRequest = UserStoryRequest.builder()
                                .name("Test User Story")
                                .role("Developer")
                                .feature("Feature A")
                                .benefit("Benefit A")
                                .priority(1)
                                .status(null)
                                .build();

                UserStory userStory = UserStory.builder()
                                .id(UUID.randomUUID())
                                .name("Test User Story")
                                .build();

                UserStoryResponse userStoryResponse = UserStoryResponse.builder()
                                .id(userStory.getId())
                                .name(userStory.getName())
                                .build();

                // Initialize the userStories list in the product backlog as mutable
                productBacklog.setUserStories(new ArrayList<>());

                when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));
                when(userStoryMapper.toEntity(userStoryRequest)).thenReturn(userStory);
                when(userStoryMapper.toResponse(userStory)).thenReturn(userStoryResponse);

                // Act
                UserStoryResponse result = productBacklogService.addUserStoryToProductBacklog(productBacklogId,
                                userStoryRequest);

                // Assert
                assertNotNull(result);
                assertEquals(userStoryResponse.getId(), result.getId());
                assertEquals(userStoryResponse.getName(), result.getName());
                verify(userStoryMapper).toEntity(userStoryRequest);
                verify(productBacklogRepository).save(productBacklog);
        }

        @Test
        void testGetProjectByProductBacklogId() {
                // Arrange
                Project project = Project.builder()
                                .id(UUID.randomUUID())
                                .name("Test Project")
                                .build();
                ProjectResponse projectResponse = ProjectResponse.builder()
                                .id(project.getId())
                                .name(project.getName())
                                .build();

                // Set the project directly on the product backlog
                productBacklog.setProject(project);

                when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));
                when(projectMapper.toResponse(project)).thenReturn(projectResponse);

                // Act
                ProjectResponse result = productBacklogService.getProjectByProductBacklogId(productBacklogId);

                // Assert
                assertNotNull(result);
                assertEquals(projectResponse.getId(), result.getId());
                assertEquals(projectResponse.getName(), result.getName());
                verify(projectMapper).toResponse(project);
        }

        @Test
        void testGetProjectByProductBacklogId_NoProject() {
                // Arrange
                productBacklog.setProject(null); // Set the project to null directly

                when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));

                // Act & Assert
                assertThrows(ResourceNotFoundException.class,
                                () -> productBacklogService.getProjectByProductBacklogId(productBacklogId));
        }
}