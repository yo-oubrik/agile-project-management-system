package ma.ensa.apms.dto;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private Long id;

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
    private UserStoryPriority priority;

    @NotNull(message = "Status is required")
    private UserStoryStatus status;

    @NotNull(message = "Product Backlog ID is required")
    private Long productBacklogId;

    private Long epicId;

    @NotEmpty(message = "Acceptance Criteria are required")
    private List<AcceptanceCriteriaDTO> acceptanceCriterias;

}