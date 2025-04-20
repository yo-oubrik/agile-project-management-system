package ma.ensa.apms.modal;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptanceCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotBlank(message = "Given condition is required")
    @Size(min = 5, max = 255, message = "Given condition must be between 5 and 255 characters")
    private String given;

    @Column(name = "_when")
    @NotBlank(message = "When condition is required")
    @Size(min = 5, max = 255, message = "When condition must be between 5 and 255 characters")
    private String when;

    @Column(name = "_then")
    @NotBlank(message = "Then condition is required")
    @Size(min = 5, max = 255, message = "Then condition must be between 5 and 255 characters")
    private String then;

    @NotNull(message = "Acceptance criteria met status is required")
    private boolean met;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;

    public boolean isMet() {
        return this.met;
    }
}