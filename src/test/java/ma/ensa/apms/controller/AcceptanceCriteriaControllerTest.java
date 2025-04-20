package ma.ensa.apms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.ensa.apms.dto.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.service.AcceptanceCriteriaService;

@WebMvcTest(AcceptanceCriteriaController.class)
class AcceptanceCriteriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AcceptanceCriteriaService acceptanceCriteriaService;

    private AcceptanceCriteriaResponse acceptanceCriteriaDTO;
    private AcceptanceCriteriaRequest creationDTO;

    @BeforeEach
    void setUp() {
        acceptanceCriteriaDTO = AcceptanceCriteriaResponse.builder()
                .id(1L)
                .given("Given test condition")
                .when("When test action")
                .then("Then test result")
                .build();

        creationDTO = AcceptanceCriteriaRequest.builder()
                .given("Given test condition")
                .when("When test action")
                .then("Then test result")
                .userStoryId(1L)
                .build();
    }

    @Test
    void create_ShouldReturnCreatedAcceptanceCriteria() throws Exception {
        when(acceptanceCriteriaService.create(any())).thenReturn(acceptanceCriteriaDTO);

        mockMvc.perform(post("/api/v1/acceptance-criteria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(acceptanceCriteriaDTO.getId()))
                .andExpect(jsonPath("$.given").value(acceptanceCriteriaDTO.getGiven()));
    }

    @Test
    void findById_ShouldReturnAcceptanceCriteria() throws Exception {
        when(acceptanceCriteriaService.findById(1L)).thenReturn(acceptanceCriteriaDTO);

        mockMvc.perform(get("/api/v1/acceptance-criteria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(acceptanceCriteriaDTO.getId()));
    }

    @Test
    void findAll_ShouldReturnAllAcceptanceCriteria() throws Exception {
        when(acceptanceCriteriaService.findAll()).thenReturn(List.of(acceptanceCriteriaDTO));

        mockMvc.perform(get("/api/v1/acceptance-criteria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(acceptanceCriteriaDTO.getId()));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(acceptanceCriteriaService).delete(1L);

        mockMvc.perform(delete("/api/v1/acceptance-criteria/1"))
                .andExpect(status().isNoContent());
    }
}