package ma.ensa.apms.modal;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 10, max = 100, message = "Title must be between 10 and 100 characters")
    private String name;

    @NotBlank(message = "Role cannot be blank")
    private String role;

    @NotBlank(message = "Feature cannot be blank")
    private String feature;

    @NotBlank(message = "Benefit cannot be blank")
    private String benefit;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority is required")
    private UserStoryPriority priority;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private UserStoryStatus status;

    @NotNull(message = "Product Backlog is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_id")
    private Epic epic;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcceptanceCriteria> acceptanceCriterias;
}