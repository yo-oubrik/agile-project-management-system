package ma.ensa.apms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEndDateUpdateDto {
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
}
