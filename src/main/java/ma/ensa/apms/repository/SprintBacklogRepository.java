package ma.ensa.apms.repository;

import ma.ensa.apms.modal.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, UUID> {
}