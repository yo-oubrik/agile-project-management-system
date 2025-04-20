package ma.ensa.apms.modal;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseEntity {
    @NotNull(message = "Created At cannot be null")
    @Column(updatable = false)
    private  LocalDateTime createdAt;

    @NotNull(message = "Updated At cannot be null")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
    

