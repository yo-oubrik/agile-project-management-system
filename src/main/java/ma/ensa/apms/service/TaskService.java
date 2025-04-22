package ma.ensa.apms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.TaskEndDateUpdateDto;
import ma.ensa.apms.dto.TaskRequestDto;
import ma.ensa.apms.dto.TaskResponseDto;
import ma.ensa.apms.dto.TaskStartDateUpdateDto;
import ma.ensa.apms.dto.TaskStatusUpdateDto;
import ma.ensa.apms.modal.enums.TaskStatus;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskDto);

    List<TaskResponseDto> getAllTasks();

    TaskResponseDto getTaskById(UUID id);

    List<TaskResponseDto> getTasksByStatus(TaskStatus status);

    List<TaskResponseDto> getTasksByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    TaskResponseDto updateTask(UUID id, TaskRequestDto taskDto);

    TaskResponseDto updateTaskStatus(UUID id, TaskStatusUpdateDto statusDto);

    TaskResponseDto updateTaskStartDate(UUID id, TaskStartDateUpdateDto startDateDto);

    TaskResponseDto updateTaskEndDate(UUID id, TaskEndDateUpdateDto endDateDto);

    void deleteTask(UUID id);
}
