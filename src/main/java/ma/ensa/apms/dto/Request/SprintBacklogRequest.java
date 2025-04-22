package ma.ensa.apms.dto.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SprintBacklogRequest {
    @NotNull(message = "Name is required")
    @Max(value = 50, message = "Name must be less than 50 characters")
    @Min(value = 3, message = "Name must be at least 3 characters")
    private String name;
}
