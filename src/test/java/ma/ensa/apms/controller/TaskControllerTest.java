package ma.ensa.apms.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.ensa.apms.dto.TaskEndDateUpdateDto;
import ma.ensa.apms.dto.TaskRequestDto;
import ma.ensa.apms.dto.TaskResponseDto;
import ma.ensa.apms.dto.TaskStartDateUpdateDto;
import ma.ensa.apms.dto.TaskStatusUpdateDto;
import ma.ensa.apms.modal.enums.TaskStatus;
import ma.ensa.apms.service.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID taskId;
    private TaskRequestDto taskRequestDto;
    private TaskResponseDto taskResponseDto;
    private List<TaskResponseDto> taskResponseDtoList;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle("Test Task");
        taskRequestDto.setDescription("Test Description");
        taskRequestDto.setStartDate(LocalDateTime.now());
        taskRequestDto.setEndDate(LocalDateTime.now().plusDays(7));
        taskRequestDto.setStatus(TaskStatus.TODO);

        taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(taskId);
        taskResponseDto.setTitle("Test Task");
        taskResponseDto.setDescription("Test Description");
        taskResponseDto.setStartDate(LocalDateTime.now());
        taskResponseDto.setEndDate(LocalDateTime.now().plusDays(7));
        taskResponseDto.setStatus(TaskStatus.TODO);

        taskResponseDtoList = Arrays.asList(taskResponseDto);
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value(taskRequestDto.getTitle()));

        verify(taskService).createTask(any(TaskRequestDto.class));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(taskResponseDtoList);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(taskId.toString()));

        verify(taskService).getAllTasks();
    }

    @Test
    void getTasksByStatus_ShouldReturnTasksWithSpecifiedStatus() throws Exception {
        when(taskService.getTasksByStatus(any(TaskStatus.class))).thenReturn(taskResponseDtoList);

        mockMvc.perform(get("/api/v1/tasks/status")
                .param("status", "TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("TODO"));

        verify(taskService).getTasksByStatus(TaskStatus.TODO);
    }

    @Test
    void getTasksByDateRange_ShouldReturnTasksInSpecifiedRange() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);

        when(taskService.getTasksByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(taskResponseDtoList);

        mockMvc.perform(get("/api/v1/tasks/range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(taskService).getTasksByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getTaskById_ShouldReturnTask() throws Exception {
        when(taskService.getTaskById(taskId)).thenReturn(taskResponseDto);

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()));

        verify(taskService).getTaskById(taskId);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        when(taskService.updateTask(eq(taskId), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()));

        verify(taskService).updateTask(eq(taskId), any(TaskRequestDto.class));
    }

    @Test
    void updateTaskStatus_ShouldUpdateAndReturnTask() throws Exception {
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskService.updateTaskStatus(eq(taskId), any(TaskStatusUpdateDto.class)))
                .thenReturn(taskResponseDto);

        mockMvc.perform(patch("/api/v1/tasks/{id}/status", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk());

        verify(taskService).updateTaskStatus(eq(taskId), any(TaskStatusUpdateDto.class));
    }

    @Test
    void updateTaskStartDate_ShouldUpdateAndReturnTask() throws Exception {
        TaskStartDateUpdateDto startDateDto = new TaskStartDateUpdateDto();
        startDateDto.setStartDate(LocalDateTime.now());

        when(taskService.updateTaskStartDate(eq(taskId), any(TaskStartDateUpdateDto.class)))
                .thenReturn(taskResponseDto);

        mockMvc.perform(patch("/api/v1/tasks/{id}/start-date", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(startDateDto)))
                .andExpect(status().isOk());

        verify(taskService).updateTaskStartDate(eq(taskId), any(TaskStartDateUpdateDto.class));
    }

    @Test
    void updateTaskEndDate_ShouldUpdateAndReturnTask() throws Exception {
        TaskEndDateUpdateDto endDateDto = new TaskEndDateUpdateDto();
        endDateDto.setEndDate(LocalDateTime.now().plusDays(10));

        when(taskService.updateTaskEndDate(eq(taskId), any(TaskEndDateUpdateDto.class)))
                .thenReturn(taskResponseDto);

        mockMvc.perform(patch("/api/v1/tasks/{id}/end-date", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(endDateDto)))
                .andExpect(status().isOk());

        verify(taskService).updateTaskEndDate(eq(taskId), any(TaskEndDateUpdateDto.class));
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(taskId);
    }
}
