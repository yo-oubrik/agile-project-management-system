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
public class AcceptanceCriteriaResponse {
    private UUID id;
    private String given;
    private String when;
    private String then;
    private boolean met;
}