package ma.ensa.apms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpicDTO {
    private Long id;
    private String name;
    private String description;
    private int priority;
    private String status;
}
