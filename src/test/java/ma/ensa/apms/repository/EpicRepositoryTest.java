package ma.ensa.apms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ma.ensa.apms.modal.Epic;

@DataJpaTest
class EpicRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EpicRepository repository;

    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void setUp() {
        epic1 = Epic.builder()
                .name("Epic 1")
                .description("Description for Epic 1")
                .build();
        epic1 = entityManager.persist(epic1);

        epic2 = Epic.builder()
                .name("Epic 2")
                .description("Description for Epic 2")
                .build();
        epic2 = entityManager.persist(epic2);

        entityManager.flush();
    }

    @Test
    void save_ShouldPersistEpic() {
        Epic newEpic = Epic.builder()
                .name("New Epic")
                .description("Description for New Epic")
                .build();

        Epic saved = repository.save(newEpic);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(newEpic.getName());
        assertThat(saved.getDescription()).isEqualTo(newEpic.getDescription());
    }

    @Test
    void findById_ShouldReturnEpic() {
        Epic found = repository.findById(epic1.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(epic1.getId());
        assertThat(found.getName()).isEqualTo(epic1.getName());
    }

    @Test
    void findAll_ShouldReturnAllEpics() {
        List<Epic> epics = repository.findAll();

        assertThat(epics).hasSize(2);
        assertThat(epics).contains(epic1, epic2);
    }

    @Test
    void deleteById_ShouldRemoveEpic() {
        repository.deleteById(epic1.getId());
        entityManager.flush();

        Epic found = repository.findById(epic1.getId()).orElse(null);
        assertThat(found).isNull();
    }
}