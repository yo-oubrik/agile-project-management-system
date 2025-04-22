package ma.ensa.apms.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SprintBacklogResponse {
    private UUID id;
    private String name;
}