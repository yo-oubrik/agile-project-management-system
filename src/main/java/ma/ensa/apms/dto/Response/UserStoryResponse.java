package ma.ensa.apms.dto.Response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryResponse {
    private UUID id;
    private String name;
    private String role;
    private String feature;
    private String benefit;
    private int priority;
    private UserStoryStatus status;
}
