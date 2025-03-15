package ma.ensa.apms.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;

    @OneToOne
    private Project project;

    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStory> userStories;

}
