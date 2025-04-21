package ma.ensa.apms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.AcceptanceCriteria;

@Repository
public interface AcceptanceCriteriaRepository extends JpaRepository<AcceptanceCriteria, UUID> {
    List<AcceptanceCriteria> findByMet(boolean met);
}