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
public class EpicResponse {
    private UUID id;
    private String name;
    private String description;
    private int userStoriesCount;
}
