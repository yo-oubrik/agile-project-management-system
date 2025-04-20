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
public class UserStoryResponse {
    private UUID id;
    private String title;
    private String description;
    private int priority;
    private int status;
    private Integer storyPoints;
    private String acceptanceCriteria;
}