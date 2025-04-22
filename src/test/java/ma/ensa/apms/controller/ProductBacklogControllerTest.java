package ma.ensa.apms.controller;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.ProductBacklogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductBacklogController.class)
class ProductBacklogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductBacklogService productBacklogService;

    private ProductBacklogRequest productBacklogRequest;
    private ProductBacklogResponse productBacklogResponse;
    private UserStoryRequest userStoryRequest;
    private UserStoryResponse userStoryResponse;
    private EpicResponse epicResponse;
    private ProjectResponse projectResponse;

    @BeforeEach
    void setUp() {
        productBacklogRequest = ProductBacklogRequest.builder()
                .name("Product Backlog 1")
                .build();

        productBacklogResponse = ProductBacklogResponse.builder()
                .id(UUID.randomUUID())
                .name("Product Backlog 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userStoryCount(5)
                .epicCount(2)
                .build();

        userStoryRequest = UserStoryRequest.builder()
                .name("As a user, I want to log in")
                .role("User")
                .feature("Log in functionality")
                .benefit("Access the system")
                .priority(1)
                .status(UserStoryStatus.TODO)
                .build();

        userStoryResponse = UserStoryResponse.builder()
                .id(UUID.randomUUID())
                .name("As a user, I want to log in")
                .role("User")
                .feature("Log in functionality")
                .benefit("Access the system")
                .priority(1)
                .status(UserStoryStatus.TODO)
                .build();

        epicResponse = EpicResponse.builder()
                .id(UUID.randomUUID())
                .name("Epic 1")
                .description("Epic description")
                .userStoriesCount(3)
                .build();

        projectResponse = ProjectResponse.builder()
                .id(UUID.randomUUID())
                .name("Project 1")
                .description("Project description")
                .status(ma.ensa.apms.modal.enums.ProjectStatus.IN_PROGRESS)
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().plusDays(20))
                .build();
    }

    @Test
    void createProductBacklog() throws Exception {
        Mockito.when(productBacklogService.create(any(ProductBacklogRequest.class)))
                .thenReturn(productBacklogResponse);

        mockMvc.perform(post("/api/v1/product-backlogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Product Backlog 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Product Backlog 1"));
    }

    @Test
    void getProductBacklogById() throws Exception {
        Mockito.when(productBacklogService.getProductBacklogById(any(UUID.class)))
                .thenReturn(productBacklogResponse);

        mockMvc.perform(get("/api/v1/product-backlogs/{id}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Product Backlog 1"));
    }

    @Test
    void deleteProductBacklog() throws Exception {
        Mockito.doNothing().when(productBacklogService).deleteProductBacklog(any(UUID.class));

        mockMvc.perform(delete("/api/v1/product-backlogs/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllProductBacklogs() throws Exception {
        Mockito.when(productBacklogService.getAllProductBacklogs())
                .thenReturn(Collections.singletonList(productBacklogResponse));

        mockMvc.perform(get("/api/v1/product-backlogs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Product Backlog 1"));
    }

    @Test
    void getUserStoriesByProductBacklogId() throws Exception {
        Mockito.when(productBacklogService.getUserStoriesByProductBacklogId(any(UUID.class)))
                .thenReturn(List.of(userStoryResponse));

        mockMvc.perform(get("/api/v1/product-backlogs/{id}/user-stories", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("As a user, I want to log in"));
    }

    @Test
    void getEpicsByProductBacklogId() throws Exception {
        Mockito.when(productBacklogService.getEpicsByProductBacklogId(any(UUID.class)))
                .thenReturn(List.of(epicResponse));

        mockMvc.perform(get("/api/v1/product-backlogs/{id}/epics", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Epic 1"));
    }

    @Test
    void addEpicToProductBacklog() throws Exception {
        Mockito.when(productBacklogService.addEpicToProductBacklog(any(UUID.class), any(EpicRequest.class)))
                .thenReturn(epicResponse);

        mockMvc.perform(post("/api/v1/product-backlogs/{id}/epics", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Epic 1\",\"description\":\"Epic description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Epic 1"));
    }

    @Test
    void addUserStoryToProductBacklog() throws Exception {
        Mockito.when(productBacklogService.addUserStoryToProductBacklog(any(UUID.class), any(UserStoryRequest.class)))
                .thenReturn(userStoryResponse);

        mockMvc.perform(post("/api/v1/product-backlogs/{id}/user-stories", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"As a user, I want to log in\",\"role\":\"User\",\"feature\":\"Log in functionality\",\"benefit\":\"Access the system\",\"priority\":1,\"status\":\"TODO\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("As a user, I want to log in"));
    }

    @Test
    void getProjectByProductBacklogId() throws Exception {
        Mockito.when(productBacklogService.getProjectByProductBacklogId(any(UUID.class)))
                .thenReturn(projectResponse);

        mockMvc.perform(get("/api/v1/product-backlogs/{id}/project", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Project 1"));
    }
}