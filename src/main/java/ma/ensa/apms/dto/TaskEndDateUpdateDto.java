package ma.ensa.apms.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEndDateUpdateDto {
    @NotNull(message = "End date cannot be null")
    private LocalDateTime endDate;
}
