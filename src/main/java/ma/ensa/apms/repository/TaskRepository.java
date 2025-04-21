package ma.ensa.apms.repository;

import ma.ensa.apms.modal.Task;
import ma.ensa.apms.modal.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByStatus(TaskStatus status);

    List<Task> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate startDate, LocalDate endDate);

    List<Task> findByStartDateBetweenOrEndDateBetween(LocalDate startDate, LocalDate endDate);

}
