package ma.ensa.apms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.TaskEndDateUpdateDto;
import ma.ensa.apms.dto.TaskRequestDto;
import ma.ensa.apms.dto.TaskResponseDto;
import ma.ensa.apms.dto.TaskStartDateUpdateDto;
import ma.ensa.apms.dto.TaskStatusUpdateDto;
import ma.ensa.apms.modal.enums.TaskStatus;
import ma.ensa.apms.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskDto) {
        TaskResponseDto createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/range")
    public ResponseEntity<List<TaskResponseDto>> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(taskService.getTasksByDateRange(startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable UUID id, @RequestBody TaskRequestDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable UUID id,
            @RequestBody TaskStatusUpdateDto statusDto) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, statusDto));
    }

    @PatchMapping("/{id}/start-date")
    public ResponseEntity<TaskResponseDto> updateTaskStartDate(@PathVariable UUID id,
            @Valid @RequestBody TaskStartDateUpdateDto startDateDto) {
        TaskResponseDto updatedTask = taskService.updateTaskStartDate(id, startDateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/end-date")
    public ResponseEntity<TaskResponseDto> updateTaskEndDate(@PathVariable UUID id,
            @Valid @RequestBody TaskEndDateUpdateDto endDateDto) {
        TaskResponseDto updatedTask = taskService.updateTaskEndDate(id, endDateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
