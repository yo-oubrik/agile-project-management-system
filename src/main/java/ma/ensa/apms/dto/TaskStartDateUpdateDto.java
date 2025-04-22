package ma.ensa.apms.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStartDateUpdateDto {
    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;
}
