package ma.ensa.apms.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.AcceptanceCriteriaService;

@WebMvcTest(AcceptanceCriteriaController.class)
class AcceptanceCriteriaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private AcceptanceCriteriaService acceptanceCriteriaService;

        @Autowired
        private ObjectMapper objectMapper;

        private UUID testId;
        private AcceptanceCriteriaRequest testRequest;
        private AcceptanceCriteriaResponse testResponse;
        private UserStoryResponse testUserStoryResponse;

        @BeforeEach
        void setUp() {
                testId = UUID.randomUUID();

                testRequest = AcceptanceCriteriaRequest.builder()
                                .given("Given a user is logged in")
                                .when("When the user clicks the logout button")
                                .then("Then the user should be logged out")
                                .met(false)
                                .build();

                testResponse = AcceptanceCriteriaResponse.builder()
                                .id(testId)
                                .given("Given a user is logged in")
                                .when("When the user clicks the logout button")
                                .then("Then the user should be logged out")
                                .met(false)
                                .build();

                testUserStoryResponse = UserStoryResponse.builder()
                                .id(UUID.randomUUID())
                                .name("User Logout")
                                .role("User")
                                .feature("Logout")
                                .benefit("Security")
                                .priority(1)
                                .status(UserStoryStatus.IN_PROGRESS)
                                .build();
        }

        @Test
        void create_ShouldCreateAcceptanceCriteria() throws Exception {
                when(acceptanceCriteriaService.create(any(AcceptanceCriteriaRequest.class))).thenReturn(testResponse);

                mockMvc.perform(post("/api/v1/acceptance-criteria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id", is(testId.toString())))
                                .andExpect(jsonPath("$.given", is(testResponse.getGiven())))
                                .andExpect(jsonPath("$.when", is(testResponse.getWhen())))
                                .andExpect(jsonPath("$.then", is(testResponse.getThen())))
                                .andExpect(jsonPath("$.met", is(testResponse.isMet())));

                verify(acceptanceCriteriaService).create(any(AcceptanceCriteriaRequest.class));
        }

        @Test
        void findById_ShouldReturnAcceptanceCriteria() throws Exception {
                when(acceptanceCriteriaService.findById(testId)).thenReturn(testResponse);

                mockMvc.perform(get("/api/v1/acceptance-criteria/{id}", testId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(testId.toString())))
                                .andExpect(jsonPath("$.given", is(testResponse.getGiven())))
                                .andExpect(jsonPath("$.when", is(testResponse.getWhen())))
                                .andExpect(jsonPath("$.then", is(testResponse.getThen())))
                                .andExpect(jsonPath("$.met", is(testResponse.isMet())));

                verify(acceptanceCriteriaService).findById(testId);
        }

        @Test
        void findAllByMet_ShouldReturnFilteredAcceptanceCriteria() throws Exception {
                List<AcceptanceCriteriaResponse> testResponses = Arrays.asList(testResponse);
                when(acceptanceCriteriaService.findAllByMet(false)).thenReturn(testResponses);

                mockMvc.perform(get("/api/v1/acceptance-criteria")
                                .param("met", "false"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id", is(testId.toString())))
                                .andExpect(jsonPath("$[0].given", is(testResponse.getGiven())))
                                .andExpect(jsonPath("$[0].when", is(testResponse.getWhen())))
                                .andExpect(jsonPath("$[0].then", is(testResponse.getThen())))
                                .andExpect(jsonPath("$[0].met", is(testResponse.isMet())));

                verify(acceptanceCriteriaService).findAllByMet(false);
        }

        @Test
        void update_ShouldUpdateAcceptanceCriteria() throws Exception {
                when(acceptanceCriteriaService.update(eq(testId), any(AcceptanceCriteriaRequest.class)))
                                .thenReturn(testResponse);

                mockMvc.perform(put("/api/v1/acceptance-criteria/{id}", testId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(testId.toString())))
                                .andExpect(jsonPath("$.given", is(testResponse.getGiven())))
                                .andExpect(jsonPath("$.when", is(testResponse.getWhen())))
                                .andExpect(jsonPath("$.then", is(testResponse.getThen())))
                                .andExpect(jsonPath("$.met", is(testResponse.isMet())));

                verify(acceptanceCriteriaService).update(eq(testId), any(AcceptanceCriteriaRequest.class));
        }

        @Test
        void delete_ShouldDeleteAcceptanceCriteria() throws Exception {
                doNothing().when(acceptanceCriteriaService).delete(testId);

                mockMvc.perform(delete("/api/v1/acceptance-criteria/{id}", testId))
                                .andExpect(status().isNoContent());

                verify(acceptanceCriteriaService).delete(testId);
        }

        @Test
        void updateMet_ShouldUpdateMetStatus() throws Exception {
                AcceptanceCriteriaResponse updatedResponse = AcceptanceCriteriaResponse.builder()
                                .id(testId)
                                .given(testResponse.getGiven())
                                .when(testResponse.getWhen())
                                .then(testResponse.getThen())
                                .met(true)
                                .build();

                when(acceptanceCriteriaService.updateMet(eq(testId), eq(true))).thenReturn(updatedResponse);

                mockMvc.perform(patch("/api/v1/acceptance-criteria/{id}/met", testId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("true"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(testId.toString())))
                                .andExpect(jsonPath("$.met", is(true)));

                verify(acceptanceCriteriaService).updateMet(testId, true);
        }

        @Test
        void getUserStory_ShouldReturnUserStory() throws Exception {
                when(acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(testId))
                                .thenReturn(testUserStoryResponse);

                mockMvc.perform(get("/api/v1/acceptance-criteria/{id}/user-story", testId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(testUserStoryResponse.getId().toString())))
                                .andExpect(jsonPath("$.name", is(testUserStoryResponse.getName())))
                                .andExpect(jsonPath("$.role", is(testUserStoryResponse.getRole())))
                                .andExpect(jsonPath("$.feature", is(testUserStoryResponse.getFeature())))
                                .andExpect(jsonPath("$.benefit", is(testUserStoryResponse.getBenefit())))
                                .andExpect(jsonPath("$.priority", is(testUserStoryResponse.getPriority())))
                                .andExpect(jsonPath("$.status", is(testUserStoryResponse.getStatus().toString())));

                verify(acceptanceCriteriaService).getUserStoryByAcceptanceCriteriaId(testId);
        }

        @Test
        void create_WithInvalidInput_ShouldReturn400() throws Exception {
                AcceptanceCriteriaRequest invalidRequest = AcceptanceCriteriaRequest.builder()
                                .given("") // Invalid: empty string
                                .when("When the user clicks the logout button")
                                .then("Then the user should be logged out")
                                .met(false)
                                .build();

                mockMvc.perform(post("/api/v1/acceptance-criteria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                verify(acceptanceCriteriaService, never()).create(any(AcceptanceCriteriaRequest.class));
        }
}
