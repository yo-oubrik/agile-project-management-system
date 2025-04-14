package ma.ensa.apms.dto;

import java.util.Date;

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
    private Long projectId;

    // The Value is The Current Time
    private Date creationDate ;

    private Date updatedAt ;

    // private List<UserStory> userStories;
    // private List<Epic> epics;
}
