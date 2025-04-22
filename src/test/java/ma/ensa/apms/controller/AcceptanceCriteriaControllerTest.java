package ma.ensa.apms.controller;

import static org.hamcrest.Matchers.hasSize;
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
import ma.ensa.apms.service.AcceptanceCriteriaService;

@WebMvcTest(controllers = AcceptanceCriteriaController.class)
class AcceptanceCriteriaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private AcceptanceCriteriaService acceptanceCriteriaService;

        private UUID id;
        private AcceptanceCriteriaRequest requestDto;
        private AcceptanceCriteriaResponse responseDto;
        private UserStoryResponse userStoryResponse;
        private List<AcceptanceCriteriaResponse> responseDtoList;

        @BeforeEach
        void setUp() {
                id = UUID.randomUUID();

                requestDto = AcceptanceCriteriaRequest.builder()
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

                userStoryResponse = UserStoryResponse.builder()
                                .id(UUID.randomUUID())
                                .name("Login Feature")
                                .role("User")
                                .feature("Authentication")
                                .benefit("Access the system")
                                .priority(1)
                                .build();

                responseDtoList = Arrays.asList(responseDto);
        }

        @Test
        void testCreate_Success() throws Exception {
                when(acceptanceCriteriaService.create(any(AcceptanceCriteriaRequest.class))).thenReturn(responseDto);

                mockMvc.perform(post("/acceptance-criteria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(id.toString()))
                                .andExpect(jsonPath("$.given").value("Given a user is logged in"))
                                .andExpect(jsonPath("$.when").value("When the user clicks on the logout button"))
                                .andExpect(jsonPath("$.then").value("Then the user is logged out"))
                                .andExpect(jsonPath("$.met").value(false));

                verify(acceptanceCriteriaService).create(any(AcceptanceCriteriaRequest.class));
        }

        @Test
        void testCreate_InvalidRequest() throws Exception {
                AcceptanceCriteriaRequest invalidRequest = AcceptanceCriteriaRequest.builder()
                                .given("") // Invalid - blank
                                .when("When") // Invalid - too short
                                .then("Then") // Invalid - too short
                                .met(false)
                                .build();

                mockMvc.perform(post("/acceptance-criteria")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                verify(acceptanceCriteriaService, never()).create(any());
        }

        @Test
        void testFindById_Success() throws Exception {
                when(acceptanceCriteriaService.findById(id)).thenReturn(responseDto);

                mockMvc.perform(get("/acceptance-criteria/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id.toString()))
                                .andExpect(jsonPath("$.given").value("Given a user is logged in"))
                                .andExpect(jsonPath("$.when").value("When the user clicks on the logout button"))
                                .andExpect(jsonPath("$.then").value("Then the user is logged out"))
                                .andExpect(jsonPath("$.met").value(false));

                verify(acceptanceCriteriaService).findById(id);
        }

        @Test
        void testFindAllByMet_Success() throws Exception {
                when(acceptanceCriteriaService.findAllByMet(true)).thenReturn(responseDtoList);

                mockMvc.perform(get("/acceptance-criteria")
                                .param("met", "true"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id").value(id.toString()));

                verify(acceptanceCriteriaService).findAllByMet(true);
        }

        @Test
        void testUpdate_Success() throws Exception {
                when(acceptanceCriteriaService.update(eq(id), any(AcceptanceCriteriaRequest.class)))
                                .thenReturn(responseDto);

                mockMvc.perform(put("/acceptance-criteria/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id.toString()));

                verify(acceptanceCriteriaService).update(eq(id), any(AcceptanceCriteriaRequest.class));
        }

        @Test
        void testUpdate_InvalidRequest() throws Exception {
                AcceptanceCriteriaRequest invalidRequest = AcceptanceCriteriaRequest.builder()
                                .given("") // Invalid - blank
                                .when("When") // Invalid - too short
                                .then("Then") // Invalid - too short
                                .met(false)
                                .build();

                mockMvc.perform(put("/acceptance-criteria/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                verify(acceptanceCriteriaService, never()).update(any(), any());
        }

        @Test
        void testDelete_Success() throws Exception {
                doNothing().when(acceptanceCriteriaService).delete(id);

                mockMvc.perform(delete("/acceptance-criteria/{id}", id))
                                .andExpect(status().isNoContent());

                verify(acceptanceCriteriaService).delete(id);
        }

        @Test
        void testUpdateMet_Success() throws Exception {
                when(acceptanceCriteriaService.updateMet(id, true)).thenReturn(responseDto);

                mockMvc.perform(patch("/acceptance-criteria/{id}/met", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("true"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id.toString()));

                verify(acceptanceCriteriaService).updateMet(id, true);
        }

        @Test
        void testGetUserStory_Success() throws Exception {
                when(acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id)).thenReturn(userStoryResponse);

                mockMvc.perform(get("/acceptance-criteria/{id}/user-story", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(userStoryResponse.getId().toString()))
                                .andExpect(jsonPath("$.name").value("Login Feature"))
                                .andExpect(jsonPath("$.role").value("User"))
                                .andExpect(jsonPath("$.feature").value("Authentication"))
                                .andExpect(jsonPath("$.benefit").value("Access the system"))
                                .andExpect(jsonPath("$.priority").value(1));

                verify(acceptanceCriteriaService).getUserStoryByAcceptanceCriteriaId(id);
        }
}
