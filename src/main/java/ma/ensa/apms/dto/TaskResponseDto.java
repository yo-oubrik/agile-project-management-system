package ma.ensa.apms.dto;

import lombok.Data;
import ma.ensa.apms.modal.enums.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TaskResponseDto {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
