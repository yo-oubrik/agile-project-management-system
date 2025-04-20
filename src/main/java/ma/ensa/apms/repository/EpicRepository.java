package ma.ensa.apms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.Epic;

@Repository
public interface EpicRepository extends JpaRepository<Epic, UUID> {

}