package ma.ensa.apms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStoryCreationDTO {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 10, max = 100, message = "Title must be between 10 and 100 characters")
    private String name;

    @NotBlank(message = "Role cannot be blank")
    private String role;

    @NotBlank(message = "Feature cannot be blank")
    private String feature;

    @NotBlank(message = "Benefit cannot be blank")
    private String benefit;

    @NotNull(message = "Priority is required")
    private String priority;

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Product Backlog is required")
    private Long productBacklogId;

    private Long epicId;
}
