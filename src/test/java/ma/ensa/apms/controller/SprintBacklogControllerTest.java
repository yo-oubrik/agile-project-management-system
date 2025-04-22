package ma.ensa.apms.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ma.ensa.apms.dto.Request.SprintBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.SprintBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.service.SprintBacklogService;

@WebMvcTest(SprintBacklogController.class)
class SprintBacklogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SprintBacklogService sprintBacklogService;

    private SprintBacklogRequest sprintBacklogRequest;
    private SprintBacklogResponse sprintBacklogResponse;
    private UserStoryRequest userStoryRequest;
    private UserStoryResponse userStoryResponse;

    @BeforeEach
    void setUp() {
        sprintBacklogRequest = SprintBacklogRequest.builder()
                .name("Sprint Backlog 1")
                .build();

        sprintBacklogResponse = SprintBacklogResponse.builder()
                .id(UUID.randomUUID())
                .name("Sprint Backlog 1")
                .build();

        userStoryRequest = UserStoryRequest.builder()
                .name("As a user, I want to log in")
                .role("User")
                .feature("Log in functionality")
                .benefit("Access the system")
                .priority(1)
                .status(ma.ensa.apms.modal.enums.UserStoryStatus.TODO)
                .build();

        userStoryResponse = UserStoryResponse.builder()
                .id(UUID.randomUUID())
                .name("As a user, I want to log in")
                .role("User")
                .feature("Log in functionality")
                .benefit("Access the system")
                .priority(1)
                .status(ma.ensa.apms.modal.enums.UserStoryStatus.TODO)
                .build();
    }

    @Test
    void createSprintBacklog() throws Exception {
        Mockito.when(sprintBacklogService.createSprintBacklog(any(SprintBacklogRequest.class)))
                .thenReturn(sprintBacklogResponse);

        mockMvc.perform(post("/api/v1/sprint-backlogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Sprint Backlog 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Sprint Backlog 1"));
    }

    @Test
    void getSprintBacklogById() throws Exception {
        Mockito.when(sprintBacklogService.getSprintBacklogById(any(UUID.class)))
                .thenReturn(sprintBacklogResponse);

        mockMvc.perform(get("/api/v1/sprint-backlogs/{id}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Sprint Backlog 1"));
    }

    @Test
    void getAllSprintBacklogs() throws Exception {
        Mockito.when(sprintBacklogService.getAllSprintBacklogs())
                .thenReturn(Collections.singletonList(sprintBacklogResponse));

        mockMvc.perform(get("/api/v1/sprint-backlogs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Sprint Backlog 1"));
    }

    @Test
    void updateSprintBacklog() throws Exception {
        Mockito.when(sprintBacklogService.updateSprintBacklog(any(UUID.class), any(SprintBacklogRequest.class)))
                .thenReturn(sprintBacklogResponse);

        mockMvc.perform(put("/api/v1/sprint-backlogs/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Sprint Backlog\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Sprint Backlog 1"));
    }

    @Test
    void deleteSprintBacklog() throws Exception {
        Mockito.doNothing().when(sprintBacklogService).deleteSprintBacklog(any(UUID.class));

        mockMvc.perform(delete("/api/v1/sprint-backlogs/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserStoriesBySprintBacklogId() throws Exception {
        Mockito.when(sprintBacklogService.getUserStoriesBySprintBacklogId(any(UUID.class)))
                .thenReturn(List.of(userStoryResponse));

        mockMvc.perform(get("/api/v1/sprint-backlogs/{id}/user-stories", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("As a user, I want to log in"));
    }

    @Test
    void addUserStoryToSprintBacklog() throws Exception {
        Mockito.when(sprintBacklogService.addUserStoryToSprintBacklog(any(UUID.class), any(UserStoryRequest.class)))
                .thenReturn(userStoryResponse);

        mockMvc.perform(post("/api/v1/sprint-backlogs/{id}/user-stories", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"As a user, I want to log in\",\"role\":\"User\",\"feature\":\"Log in functionality\",\"benefit\":\"Access the system\",\"priority\":1,\"status\":\"TODO\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("As a user, I want to log in"));
    }

    @Test
    void removeUserStoryFromSprintBacklog() throws Exception {
        Mockito.doNothing().when(sprintBacklogService).removeUserStoryFromSprintBacklog(any(UUID.class),
                any(UUID.class));

        mockMvc.perform(
                delete("/api/v1/sprint-backlogs/{id}/user-stories/{userStoryId}", UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}