package ma.ensa.apms.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import ma.ensa.apms.modal.enums.TaskStatus;

@Data
public class TaskResponseDto {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
