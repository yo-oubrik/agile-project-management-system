package ma.ensa.apms.dto;

import lombok.Data;
import ma.ensa.apms.modal.enums.TaskStatus;

@Data
public class TaskStatusUpdateDto {
    private TaskStatus status;
}
