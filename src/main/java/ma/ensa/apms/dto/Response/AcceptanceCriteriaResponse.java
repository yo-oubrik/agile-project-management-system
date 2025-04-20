package ma.ensa.apms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceCriteriaResponse {
    private Long id;
    private String given;
    private String when;
    private String then;
}