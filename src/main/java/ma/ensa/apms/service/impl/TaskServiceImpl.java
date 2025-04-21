package ma.ensa.apms.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.*;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.TaskMapper;
import ma.ensa.apms.modal.Task;
import ma.ensa.apms.modal.enums.TaskStatus;
import ma.ensa.apms.repository.TaskRepository;
import ma.ensa.apms.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findByStartDateBetweenOrEndDateBetween(startDate, endDate)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(UUID id, TaskRequestDto taskDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        Task updatedTask = taskMapper.toEntity(taskDto);
        updatedTask.setId(existingTask.getId());

        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskStatus(UUID id, TaskStatusUpdateDto statusDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setStatus(statusDto.getStatus());
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskStartDate(UUID id, TaskStartDateUpdateDto startDateDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        LocalDate newStartDate = startDateDto.getStartDate();

        // Validate that new start date is not after existing end date
        if (task.getEndDate() != null && newStartDate.isAfter(task.getEndDate())) {
            throw new BusinessException("Start date cannot be after the end date");
        }

        task.setStartDate(newStartDate);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskEndDate(UUID id, TaskEndDateUpdateDto endDateDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        LocalDate newEndDate = endDateDto.getEndDate();

        if (task.getStartDate() != null && newEndDate.isBefore(task.getStartDate())) {
            throw new BusinessException("End date cannot be before the start date");
        }

        task.setEndDate(newEndDate);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }
}
