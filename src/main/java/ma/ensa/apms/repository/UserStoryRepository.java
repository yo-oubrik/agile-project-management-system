package ma.ensa.apms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.UserStory;
import java.util.List;
import ma.ensa.apms.modal.enums.UserStoryStatus;



@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    boolean existsByNameAndProductBacklogId(String name, Long productBacklogId);
    List<UserStory> findByProductBacklogIdOrderByPriorityAsc(Long productBacklogId);

    List<UserStory> findByStatusAndProductBacklogId(UserStoryStatus status , Long productBacklogId);
    List<UserStory> findByEpicId(Long epicId);

    List<UserStory> findBySprintId(Long sprintId);
}