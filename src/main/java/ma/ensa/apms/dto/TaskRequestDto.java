package ma.ensa.apms.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ma.ensa.apms.modal.enums.TaskStatus;

@Data
public class TaskRequestDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    private TaskStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
