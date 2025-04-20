package ma.ensa.apms.dto.Request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryToEpicRequest {
    @NotNull(message = "User story ID is required")
    private UUID userStoryId;
}