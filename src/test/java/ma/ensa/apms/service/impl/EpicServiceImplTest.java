package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.exception.EmptyResourcesException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.service.EpicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpicServiceImplTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private EpicMapper epicMapper;

    @InjectMocks
    private EpicServiceImpl epicService;

    private Epic epic;
    private EpicDTO epicDTO;
    private EpicCreationDTO creationDTO;

    @BeforeEach
    void setUp() {
        epic = Epic.builder()
                .id(1L)
                .name("Epic 1")
                .description("Description for Epic 1")
                .build();

        epicDTO = EpicDTO.builder()
                .id(1L)
                .name("Epic 1")
                .description("Description for Epic 1")
                .build();

        creationDTO = EpicCreationDTO.builder()
                .name("Epic 1")
                .description("Description for Epic 1")
                .build();
    }

    @Test
    void create_ShouldReturnSavedEpic() {
        when(epicMapper.toEntity(any(EpicCreationDTO.class))).thenReturn(epic);
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        EpicDTO result = epicService.create(creationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(epicRepository).save(any(Epic.class));
    }

    @Test
    void findById_ShouldReturnEpic_WhenExists() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicMapper.toDto(epic)).thenReturn(epicDTO);

        EpicDTO result = epicService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(epicRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> epicService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Epic not found");
    }

    @Test
    void findAll_ShouldReturnAllEpics() {
        when(epicRepository.findAll()).thenReturn(List.of(epic));
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        List<EpicDTO> result = epicService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void findAll_ShouldThrowEmptyResourcesException_WhenNoEpicsExist() {
        when(epicRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> epicService.findAll())
                .isInstanceOf(EmptyResourcesException.class)
                .hasMessage("No epics found");
    }

    @Test
    void update_ShouldReturnUpdatedEpic() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        EpicDTO result = epicService.update(1L, creationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(epicRepository).save(any(Epic.class));
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenEpicNotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> epicService.update(1L, creationDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Epic not found");
    }

    @Test
    void delete_ShouldDeleteSuccessfully_WhenExists() {
        when(epicRepository.existsById(1L)).thenReturn(true);

        epicService.delete(1L);

        verify(epicRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(epicRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> epicService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Epic not found");
    }
}