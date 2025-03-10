package ma.ensa.apms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.ensa.apms.dto.AcceptanceCriteriaDTO;
import ma.ensa.apms.dto.UserStoryDTO;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.UserStoryService;

@WebMvcTest(controllers = UserStoryController.class)
class UserStoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserStoryService userStoryService;

    private UserStoryDTO userStoryDTO;
    private AcceptanceCriteriaDTO acceptanceCriteriaDTO;

    @BeforeEach
    void setUp() {
        acceptanceCriteriaDTO = AcceptanceCriteriaDTO.builder()
                .id(1L)
                .given("Given a test condition")
                .when("When an action occurs")
                .then("Then expect a result")
                .build();

        userStoryDTO = UserStoryDTO.builder()
                .id(1L)
                .name("Test Story")
                .role("As a tester")
                .feature("I want to test the API")
                .benefit("So that I can ensure it works")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .productBacklogId(1L)
                .acceptanceCriterias(List.of(acceptanceCriteriaDTO))
                .build();
    }

    @Test
    void createUserStory_ShouldReturnCreatedUserStory() throws Exception {
        when(userStoryService.save(any(UserStoryDTO.class))).thenReturn(userStoryDTO);

        mockMvc.perform(post("/api/v1/user-stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userStoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userStoryDTO.getId()))
                .andExpect(jsonPath("$.name").value(userStoryDTO.getName()))
                .andExpect(jsonPath("$.acceptanceCriterias[0].given").value(acceptanceCriteriaDTO.getGiven()));
    }

    @Test
    void getUserStoryById_ShouldReturnUserStory() throws Exception {
        when(userStoryService.findById(1L)).thenReturn(userStoryDTO);

        mockMvc.perform(get("/api/v1/user-stories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryDTO.getId()))
                .andExpect(jsonPath("$.name").value(userStoryDTO.getName()));
    }

    @Test
    void getUserStoryById_ShouldReturnNotFound_WhenUserStoryDoesNotExist() throws Exception {
        when(userStoryService.findById(999L))
                .thenThrow(new ResourceNotFoundException("User Story not found"));

        mockMvc.perform(get("/api/v1/user-stories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUserStories_ShouldReturnListOfUserStories() throws Exception {
        when(userStoryService.findAll()).thenReturn(List.of(userStoryDTO));

        mockMvc.perform(get("/api/v1/user-stories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userStoryDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(userStoryDTO.getName()));
    }

    @Test
    void updateUserStory_ShouldReturnUpdatedUserStory() throws Exception {
        when(userStoryService.update(eq(1L), any(UserStoryDTO.class))).thenReturn(userStoryDTO);

        mockMvc.perform(put("/api/v1/user-stories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userStoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryDTO.getId()))
                .andExpect(jsonPath("$.name").value(userStoryDTO.getName()));
    }

    @Test
    void deleteUserStory_ShouldReturnNoContent() throws Exception {
        doNothing().when(userStoryService).delete(1L);

        mockMvc.perform(delete("/api/v1/user-stories/1"))
                .andExpect(status().isNoContent());

        verify(userStoryService).delete(1L);
    }

    @Test
    void deleteUserStory_ShouldReturnNotFound_WhenUserStoryDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("User Story not found"))
                .when(userStoryService).delete(999L);

        mockMvc.perform(delete("/api/v1/user-stories/999"))
                .andExpect(status().isNotFound());
    }
}