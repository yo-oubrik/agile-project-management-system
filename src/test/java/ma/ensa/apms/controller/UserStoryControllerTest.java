// package ma.ensa.apms.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import ma.ensa.apms.dto.UserStoryCreationDTO;
// import ma.ensa.apms.dto.UserStoryDTO;
// import ma.ensa.apms.service.UserStoryService;

// @WebMvcTest(UserStoryController.class)
// class UserStoryControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockitoBean
//     private UserStoryService userStoryService;

//     private UserStoryDTO userStoryDTO;
//     private UserStoryCreationDTO creationDTO;

//     @BeforeEach
//     void setUp() {
//         userStoryDTO = UserStoryDTO.builder()
//                 .id(1L)
//                 .name("Test Story")
//                 .role("Test Role")
//                 .feature("Test Feature")
//                 .benefit("Test Benefit")
//                 .priority("HIGH")
//                 .status("TODO")
//                 .productBacklogId(1L)
//                 .build();

//         creationDTO = UserStoryCreationDTO.builder()
//                 .name("Test Story")
//                 .role("Test Role")
//                 .feature("Test Feature")
//                 .benefit("Test Benefit")
//                 .priority("HIGH")
//                 .status("TODO")
//                 .productBacklogId(1L)
//                 .build();
//     }

//     @Test
//     void create_ShouldReturnCreatedUserStory() throws Exception {
//         when(userStoryService.create(any())).thenReturn(userStoryDTO);

//         mockMvc.perform(post("/api/v1/user-stories")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(creationDTO)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").value(userStoryDTO.getId()))
//                 .andExpect(jsonPath("$.name").value(userStoryDTO.getName()));
//     }

//     @Test
//     void findById_ShouldReturnUserStory() throws Exception {
//         when(userStoryService.findById(1L)).thenReturn(userStoryDTO);

//         mockMvc.perform(get("/api/v1/user-stories/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(userStoryDTO.getId()));
//     }

//     @Test
//     void findAll_ShouldReturnAllUserStories() throws Exception {
//         when(userStoryService.findAll()).thenReturn(List.of(userStoryDTO));

//         mockMvc.perform(get("/api/v1/user-stories"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id").value(userStoryDTO.getId()));
//     }

//     @Test
//     void delete_ShouldReturnNoContent() throws Exception {
//         doNothing().when(userStoryService).delete(1L);

//         mockMvc.perform(delete("/api/v1/user-stories/1"))
//                 .andExpect(status().isNoContent());
//     }
// }
