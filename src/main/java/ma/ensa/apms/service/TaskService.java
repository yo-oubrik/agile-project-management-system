package ma.ensa.apms.service;

import ma.ensa.apms.dto.*;
import ma.ensa.apms.modal.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskDto);

    List<TaskResponseDto> getAllTasks();

    TaskResponseDto getTaskById(UUID id);

    List<TaskResponseDto> getTasksByStatus(TaskStatus status);

    List<TaskResponseDto> getTasksByDateRange(LocalDate startDate, LocalDate endDate);

    TaskResponseDto updateTask(UUID id, TaskRequestDto taskDto);

    TaskResponseDto updateTaskStatus(UUID id, TaskStatusUpdateDto statusDto);

    TaskResponseDto updateTaskStartDate(UUID id, TaskStartDateUpdateDto startDateDto);

    TaskResponseDto updateTaskEndDate(UUID id, TaskEndDateUpdateDto endDateDto);

    void deleteTask(UUID id);
}
