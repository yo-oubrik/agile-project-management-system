package ma.ensa.apms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ma.ensa.apms.modal.UserStory;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    boolean existsByNameAndProductBacklogId(String name, Long productBacklogId);
}