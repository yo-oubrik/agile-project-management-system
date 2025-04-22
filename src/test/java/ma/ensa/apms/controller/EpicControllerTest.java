package ma.ensa.apms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.UserStoryToEpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.EpicService;

@WebMvcTest(EpicController.class)
public class EpicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EpicService epicService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID epicId;
    private EpicRequest epicRequest;
    private EpicResponse epicResponse;
    private List<EpicResponse> epicResponses;
    private UserStoryToEpicRequest userStoryToEpicRequest;
    private List<UserStoryResponse> userStoryResponses;
    private ProductBacklogResponse productBacklogResponse;

    @BeforeEach
    void setUp() {
        epicId = UUID.randomUUID();

        epicRequest = EpicRequest.builder()
                .name("Epic Name")
                .description("Epic Description")
                .build();

        epicResponse = EpicResponse.builder()
                .id(epicId)
                .name("Epic Name")
                .description("Epic Description")
                .build();

        epicResponses = Arrays.asList(epicResponse);

        UUID userStoryId = UUID.randomUUID();
        userStoryToEpicRequest = UserStoryToEpicRequest.builder()
                .userStoryId(userStoryId)
                .build();

        UserStoryResponse userStoryResponse = UserStoryResponse.builder()
                .id(userStoryId)
                .name("User Story Name")
                .role("As a user")
                .feature("I want to perform an action")
                .benefit("So that I can achieve a goal")
                .priority(1)
                .status(UserStoryStatus.TODO)
                .build();

        userStoryResponses = Arrays.asList(userStoryResponse);

        UUID productBacklogId = UUID.randomUUID();
        productBacklogResponse = ProductBacklogResponse.builder()
                .id(productBacklogId)
                .name("Product Backlog")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userStoryCount(5)
                .epicCount(2)
                .build();
    }

    @Test
    void testCreateEpic() throws Exception {
        when(epicService.create(any(EpicRequest.class))).thenReturn(epicResponse);

        mockMvc.perform(post("/api/v1/epics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(epicRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(epicId.toString()))
                .andExpect(jsonPath("$.name").value("Epic Name"))
                .andExpect(jsonPath("$.description").value("Epic Description"));
    }

    @Test
    void testFindEpicById() throws Exception {
        when(epicService.findById(any(UUID.class))).thenReturn(epicResponse);

        mockMvc.perform(get("/api/v1/epics/{id}", epicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(epicId.toString()))
                .andExpect(jsonPath("$.name").value("Epic Name"))
                .andExpect(jsonPath("$.description").value("Epic Description"));
    }

    @Test
    void testFindAllEpics() throws Exception {
        when(epicService.findAll()).thenReturn(epicResponses);

        mockMvc.perform(get("/api/v1/epics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(epicId.toString()))
                .andExpect(jsonPath("$[0].name").value("Epic Name"))
                .andExpect(jsonPath("$[0].description").value("Epic Description"));
    }

    @Test
    void testUpdateEpic() throws Exception {
        when(epicService.update(eq(epicId), any(EpicRequest.class))).thenReturn(epicResponse);

        mockMvc.perform(put("/api/v1/epics/{id}", epicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(epicRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(epicId.toString()))
                .andExpect(jsonPath("$.name").value("Epic Name"))
                .andExpect(jsonPath("$.description").value("Epic Description"));
    }

    @Test
    void testDeleteEpic() throws Exception {
        doNothing().when(epicService).delete(epicId);

        mockMvc.perform(delete("/api/v1/epics/{id}", epicId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddUserStoryToEpic() throws Exception {
        when(epicService.addUserStoryToEpic(eq(epicId), any(UUID.class))).thenReturn(epicResponse);

        mockMvc.perform(post("/api/v1/epics/{epicId}/user-stories", epicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userStoryToEpicRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(epicId.toString()))
                .andExpect(jsonPath("$.name").value("Epic Name"))
                .andExpect(jsonPath("$.description").value("Epic Description"));
    }

    @Test
    void testGetUserStories() throws Exception {
        when(epicService.getUserStoriesByEpicId(epicId)).thenReturn(userStoryResponses);

        mockMvc.perform(get("/api/v1/epics/{id}/user-stories", epicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(userStoryResponses.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].name").value("User Story Name"))
                .andExpect(jsonPath("$[0].role").value("As a user"))
                .andExpect(jsonPath("$[0].feature").value("I want to perform an action"))
                .andExpect(jsonPath("$[0].benefit").value("So that I can achieve a goal"))
                .andExpect(jsonPath("$[0].priority").value(1))
                .andExpect(jsonPath("$[0].status").value("TODO"));
    }

    @Test
    void testGetProductBacklog() throws Exception {
        when(epicService.getProductBacklogByEpicId(epicId)).thenReturn(productBacklogResponse);

        mockMvc.perform(get("/api/v1/epics/{id}/product-backlog", epicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productBacklogResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value("Product Backlog"))
                .andExpect(jsonPath("$.userStoryCount").value(5))
                .andExpect(jsonPath("$.epicCount").value(2));
    }
}
