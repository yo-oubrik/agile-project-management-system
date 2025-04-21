package ma.ensa.apms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.ensa.apms.modal.SprintBacklog;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, UUID> {
    
}
