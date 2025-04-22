package ma.ensa.apms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.UserStoryRepository;

@ExtendWith(MockitoExtension.class)
public class EpicServiceImplTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private EpicMapper epicMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @InjectMocks
    private EpicServiceImpl epicService;

    private UUID epicId;
    private Epic epic;
    private EpicRequest epicRequest;
    private EpicResponse epicResponse;
    private UserStory userStory;
    private UUID userStoryId;
    private UserStoryResponse userStoryResponse;
    private ProductBacklog productBacklog;
    private ProductBacklogResponse productBacklogResponse;
    private List<Epic> epicList;
    private List<UserStory> userStoryList;

    @BeforeEach
    void setUp() {
        // Initialize test data
        epicId = UUID.randomUUID();
        userStoryId = UUID.randomUUID();

        epicRequest = new EpicRequest();
        epicRequest.setName("Test Epic");
        epicRequest.setDescription("Test Epic Description");

        epic = new Epic();
        epic.setId(epicId);
        epic.setName("Test Epic");
        epic.setDescription("Test Epic Description");

        userStory = new UserStory();
        userStory.setId(userStoryId);

        userStoryResponse = new UserStoryResponse();
        userStoryResponse.setId(userStoryId);

        epicResponse = new EpicResponse();
        epicResponse.setId(epicId);
        epicResponse.setName("Test Epic");
        epicResponse.setDescription("Test Epic Description");

        productBacklog = new ProductBacklog();
        productBacklog.setId(UUID.randomUUID());

        productBacklogResponse = new ProductBacklogResponse();
        productBacklogResponse.setId(productBacklog.getId());

        userStoryList = new ArrayList<>();
        userStoryList.add(userStory);
        epic.setUserStories(userStoryList);
        epic.setProductBacklog(productBacklog);

        epicList = new ArrayList<>();
        epicList.add(epic);
    }

    @Test
    void testCreate() {
        // Setup
        when(epicMapper.toEntity(epicRequest)).thenReturn(epic);
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.create(epicRequest);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        verify(epicRepository).save(epic);
        verify(epicMapper).toEntity(epicRequest);
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testFindById() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.findById(epicId);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        assertEquals(1, result.getUserStoriesCount());
        verify(epicRepository).findById(epicId);
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testFindById_NotFound() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class, () -> epicService.findById(epicId));
        verify(epicRepository).findById(epicId);
    }

    @Test
    void testFindAll() {
        // Setup
        when(epicRepository.findAll()).thenReturn(epicList);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        List<EpicResponse> result = epicService.findAll();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(epicRepository).findAll();
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testUpdate() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.update(epicId, epicRequest);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        verify(epicRepository).findById(epicId);
        verify(epicMapper).updateEntityFromDto(epicRequest, epic);
        verify(epicRepository).save(epic);
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testDelete() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));

        // Execute
        epicService.delete(epicId);

        // Verify
        verify(epicRepository).findById(epicId);
        verify(epicRepository).delete(epic);
    }

    @Test
    void testAddUserStoryToEpic() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.addUserStoryToEpic(epicId, userStoryId);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        verify(epicRepository).findById(epicId);
        verify(userStoryRepository).findById(userStoryId);
        verify(userStoryRepository).save(userStory);
        verify(epicMapper).toDto(epic);
        assertEquals(epic, userStory.getEpic());
    }

    @Test
    void testGetUserStoriesByEpicId() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
        when(userStoryMapper.toResponse(userStory)).thenReturn(userStoryResponse);

        // Execute
        List<UserStoryResponse> result = epicService.getUserStoriesByEpicId(epicId);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(epicRepository).findById(epicId);
        verify(userStoryMapper).toResponse(userStory);
    }

    @Test
    void testGetProductBacklogByEpicId() {
        // Setup
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));
        when(productBacklogMapper.toResponse(productBacklog)).thenReturn(productBacklogResponse);

        // Execute
        ProductBacklogResponse result = epicService.getProductBacklogByEpicId(epicId);

        // Verify
        assertNotNull(result);
        assertEquals(productBacklogResponse, result);
        verify(epicRepository).findById(epicId);
        verify(productBacklogMapper).toResponse(productBacklog);
    }

    @Test
    void testGetProductBacklogByEpicId_NoProductBacklog() {
        // Setup
        Epic epicWithoutBacklog = new Epic();
        epicWithoutBacklog.setId(epicId);

        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epicWithoutBacklog));

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class,
                () -> epicService.getProductBacklogByEpicId(epicId));
        verify(epicRepository).findById(epicId);
    }

    @Test
    void testCountEpics() {
        // Setup
        when(epicRepository.count()).thenReturn(5L);

        // Execute
        long result = epicService.countEpics();

        // Verify
        assertEquals(5L, result);
        verify(epicRepository).count();
    }
}
