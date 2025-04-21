package ma.ensa.apms.dto;

import java.time.LocalDate;

import lombok.Data;
import ma.ensa.apms.modal.enums.TaskStatus;

@Data
public class TaskRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
