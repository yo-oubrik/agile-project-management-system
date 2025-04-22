package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptanceCriteriaServiceImplTest {

    @Mock
    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;

    @Mock
    private AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @InjectMocks
    private AcceptanceCriteriaServiceImpl acceptanceCriteriaService;

    private UUID id;
    private AcceptanceCriteriaRequest requestDto;
    private AcceptanceCriteria entity;
    private AcceptanceCriteriaResponse responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();

        requestDto = AcceptanceCriteriaRequest.builder()
                .given("Given a user is logged in")
                .when("When the user clicks on the logout button")
                .then("Then the user is logged out")
                .met(false)
                .build();

        entity = AcceptanceCriteria.builder()
                .id(id)
                .given("Given a user is logged in")
                .when("When the user clicks on the logout button")
                .then("Then the user is logged out")
                .met(false)
                .build();

        responseDto = AcceptanceCriteriaResponse.builder()
                .id(id)
                .given("Given a user is logged in")
                .when("When the user clicks on the logout button")
                .then("Then the user is logged out")
                .met(false)
                .build();
    }

    @Test
    void testCreate() {
        // Arrange
        when(acceptanceCriteriaMapper.toEntity(requestDto)).thenReturn(entity);
        when(acceptanceCriteriaRepository.save(entity)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(acceptanceCriteriaMapper).toEntity(requestDto);
        verify(acceptanceCriteriaRepository).save(entity);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testFindById_ExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(acceptanceCriteriaRepository).findById(id);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testFindById_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.findById(id));
        verify(acceptanceCriteriaRepository).findById(id);
    }

    @Test
    void testFindAllByMet() {
        // Arrange
        boolean metStatus = true;
        List<AcceptanceCriteria> entities = Arrays.asList(entity);
        when(acceptanceCriteriaRepository.findByMet(metStatus)).thenReturn(entities);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        List<AcceptanceCriteriaResponse> results = acceptanceCriteriaService.findAllByMet(metStatus);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(acceptanceCriteriaRepository).findByMet(metStatus);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<AcceptanceCriteria> entities = Arrays.asList(entity);
        when(acceptanceCriteriaRepository.findAll()).thenReturn(entities);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        List<AcceptanceCriteriaResponse> results = acceptanceCriteriaService.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(acceptanceCriteriaRepository).findAll();
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testUpdate_ExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(acceptanceCriteriaMapper).updateEntityFromDto(requestDto, entity);
        when(acceptanceCriteriaRepository.save(entity)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.update(id, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(acceptanceCriteriaRepository).findById(id);
        verify(acceptanceCriteriaMapper).updateEntityFromDto(requestDto, entity);
        verify(acceptanceCriteriaRepository).save(entity);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testUpdate_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.update(id, requestDto));
        verify(acceptanceCriteriaRepository).findById(id);
        verify(acceptanceCriteriaMapper, never()).updateEntityFromDto(any(), any());
    }

    @Test
    void testDelete_ExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.existsById(id)).thenReturn(true);
        doNothing().when(acceptanceCriteriaRepository).deleteById(id);

        // Act
        acceptanceCriteriaService.delete(id);

        // Assert
        verify(acceptanceCriteriaRepository).existsById(id);
        verify(acceptanceCriteriaRepository).deleteById(id);
    }

    @Test
    void testDelete_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.delete(id));
        verify(acceptanceCriteriaRepository).existsById(id);
        verify(acceptanceCriteriaRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateMet_ExistingId() {
        // Arrange
        boolean newMetStatus = true;
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(acceptanceCriteriaRepository.save(entity)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.updateMet(id, newMetStatus);

        // Assert
        assertNotNull(result);
        verify(acceptanceCriteriaRepository).findById(id);
        verify(acceptanceCriteriaRepository).save(entity);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testUpdateMet_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.updateMet(id, true));
        verify(acceptanceCriteriaRepository).findById(id);
        verify(acceptanceCriteriaRepository, never()).save(any());
    }

    @Test
    void testGetUserStoryByAcceptanceCriteriaId_ExistingIdWithUserStory() {
        // Arrange
        UserStory userStory = new UserStory();
        entity.setUserStory(userStory);

        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        UserStoryResponse result = acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id);

        // Assert
        assertNotNull(result);
        verify(acceptanceCriteriaRepository).findById(id);
    }

    @Test
    void testGetUserStoryByAcceptanceCriteriaId_ExistingIdWithoutUserStory() {
        // Arrange
        entity.setUserStory(null);
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id));
        verify(acceptanceCriteriaRepository).findById(id);
    }

    @Test
    void testGetUserStoryByAcceptanceCriteriaId_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id));
        verify(acceptanceCriteriaRepository).findById(id);
    }
}
