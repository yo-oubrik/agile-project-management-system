package ma.ensa.apms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@DataJpaTest
class UserStoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserStoryRepository repository;

    private ProductBacklog productBacklog;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        productBacklog = ProductBacklog.builder()
                .name("Test Product Backlog")
                .build();
        productBacklog = entityManager.persist(productBacklog);

        userStory = UserStory.builder()
                .name("Test User Story Implementation")
                .role("As a developer")
                .feature("I want to test the repository")
                .benefit("So that I can ensure it works correctly")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .productBacklog(productBacklog)
                .acceptanceCriterias(new ArrayList<>())
                .build();
        userStory = entityManager.persist(userStory);

        entityManager.flush();
    }

    @Test
    void save_ShouldPersistUserStory() {
        UserStory newUserStory = UserStory.builder()
                .name("New User Story")
                .role("As a user")
                .feature("I want to add a new user story")
                .benefit("So that I can test the save functionality")
                .priority(UserStoryPriority.MEDIUM)
                .status(UserStoryStatus.IN_PROGRESS)
                .productBacklog(productBacklog)
                .build();

        UserStory saved = repository.save(newUserStory);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(newUserStory.getName());
        assertThat(saved.getRole()).isEqualTo(newUserStory.getRole());
    }

    @Test
    void existsByNameAndProductBacklogId_ShouldReturnTrue() {
        boolean exists = repository.existsByNameAndProductBacklogId(userStory.getName(), productBacklog.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByNameAndProductBacklogId_ShouldReturnFalse() {
        boolean exists = repository.existsByNameAndProductBacklogId("Nonexistent User Story", productBacklog.getId());

        assertThat(exists).isFalse();
    }
}
