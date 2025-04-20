package ma.ensa.apms.dto.Response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private UUID id;
    private String name;
    private String role;
    private String feature;
    private String benefit;
    private String priority;
    private String status;
    private UUID productBacklogId;
    private UUID epicId;
}
