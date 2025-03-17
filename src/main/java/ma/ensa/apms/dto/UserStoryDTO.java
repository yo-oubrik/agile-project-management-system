package ma.ensa.apms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private Long id;
    private String name;
    private String role;
    private String feature;
    private String benefit;
    private String priority;
    private String status;
    private Long productBacklogId;
    private Long epicId;
}
