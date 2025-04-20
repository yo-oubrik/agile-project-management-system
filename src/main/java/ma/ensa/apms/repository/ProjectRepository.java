package ma.ensa.apms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByStartDateAfterAndEndDateBefore(LocalDateTime startDate, LocalDateTime endDate);
    List<Project> findByStatus(ProjectStatus status);
}
