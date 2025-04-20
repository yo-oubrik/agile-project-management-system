package ma.ensa.apms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.UserStory;
import java.util.List;
import java.util.UUID;

import ma.ensa.apms.modal.enums.UserStoryStatus;



@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, UUID> {
    boolean existsByNameAndProductBacklogId(String name, UUID productBacklogId);
    List<UserStory> findByProductBacklogIdOrderByPriorityAsc(UUID productBacklogId);

    List<UserStory> findByStatusAndProductBacklogId(UserStoryStatus status , UUID productBacklogId);
    List<UserStory> findByEpicId(UUID epicId);

    List<UserStory> findBySprintBacklogId(UUID sprintBacklogId);
}