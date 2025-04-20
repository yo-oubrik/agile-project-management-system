package ma.ensa.apms.dto.Request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcceptanceCriteriaRequest {
    @NotBlank(message = "Given condition is required")
    @Size(min = 5, max = 255, message = "Given condition must be between 5 and 255 characters")
    private String given;

    @NotBlank(message = "When condition is required")
    @Size(min = 5, max = 255, message = "When condition must be between 5 and 255 characters")
    private String when;

    @NotBlank(message = "Then condition is required")
    @Size(min = 5, max = 255, message = "Then condition must be between 5 and 255 characters")
    private String then;

    @NotNull(message = "Acceptance criteria met status is required")
    private boolean met ;

    private UUID userStoryId;
}