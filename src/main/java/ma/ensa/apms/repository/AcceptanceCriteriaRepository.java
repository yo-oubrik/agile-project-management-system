package ma.ensa.apms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ma.ensa.apms.modal.AcceptanceCriteria;

@Repository
public interface AcceptanceCriteriaRepository extends JpaRepository<AcceptanceCriteria, Long> {
    List<AcceptanceCriteria> findByUserStoryId(Long userStoryId);
}