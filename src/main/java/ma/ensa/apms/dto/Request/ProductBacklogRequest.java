package ma.ensa.apms.dto.Request;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBacklogRequest {
    @NotNull(message = "Product Backlog Name is required")
    private String name;
    @NotNull(message = "Project is required")
    private UUID projectId;
    private Date creationDate ;
    private Date updatedAt ;
}
