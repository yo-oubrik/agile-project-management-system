package ma.ensa.apms.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBacklogResponse {
    private Long id;
    private String name;

    // private ProjectResponse project;

    private Date creationDate ;

    private Date updatedAt ;

    // private List<UserStoryResponse> userStories;
    // private List<EpicResponse> epics;
}
