package ma.ensa.apms.controller;

import ma.ensa.apms.dto.EpicCreationDTO;
import ma.ensa.apms.dto.EpicDTO;
import ma.ensa.apms.service.EpicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EpicControllerTest {

    @Mock
    private EpicService epicService;

    @InjectMocks
    private EpicController epicController;

    private MockMvc mockMvc;

    private EpicDTO epicDTO;
    private EpicCreationDTO creationDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(epicController).build();

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
    void create_ShouldReturnCreatedEpic() throws Exception {
        when(epicService.create(any(EpicCreationDTO.class))).thenReturn(epicDTO);

        mockMvc.perform(post("/api/epics")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Epic 1\",\"description\":\"Description for Epic 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Epic 1"))
                .andExpect(jsonPath("$.description").value("Description for Epic 1"));

        verify(epicService).create(any(EpicCreationDTO.class));
    }

    @Test
    void findById_ShouldReturnEpic_WhenExists() throws Exception {
        when(epicService.findById(1L)).thenReturn(epicDTO);

        mockMvc.perform(get("/api/epics/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Epic 1"))
                .andExpect(jsonPath("$.description").value("Description for Epic 1"));

        verify(epicService).findById(1L);
    }

    @Test
    void findAll_ShouldReturnAllEpics() throws Exception {
        when(epicService.findAll()).thenReturn(List.of(epicDTO));

        mockMvc.perform(get("/api/epics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Epic 1"))
                .andExpect(jsonPath("$[0].description").value("Description for Epic 1"));

        verify(epicService).findAll();
    }

    @Test
    void update_ShouldReturnUpdatedEpic() throws Exception {
        when(epicService.update(eq(1L), any(EpicCreationDTO.class))).thenReturn(epicDTO);

        mockMvc.perform(put("/api/epics/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Epic\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Epic 1"))
                .andExpect(jsonPath("$.description").value("Description for Epic 1"));

        verify(epicService).update(eq(1L), any(EpicCreationDTO.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(epicService).delete(1L);

        mockMvc.perform(delete("/api/epics/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(epicService).delete(1L);
    }
}