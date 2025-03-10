package ma.ensa.apms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryPriority;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@DataJpaTest
class UserStoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserStoryRepository userStoryRepository;

    private ProductBacklog productBacklog;
    private UserStory userStory;
    private AcceptanceCriteria acceptanceCriteria;

    @BeforeEach
    void setUp() {
        productBacklog = new ProductBacklog();
        productBacklog.setName("Test Product Backlog");
        productBacklog = entityManager.persist(productBacklog);

        acceptanceCriteria = AcceptanceCriteria.builder()
                .given("Given a test condition")
                .when("When an action occurs")
                .then("Then expect a result")
                .build();
        acceptanceCriteria = entityManager.persist(acceptanceCriteria);

        userStory = UserStory.builder()
                .name("Test User Story")
                .role("As a tester")
                .feature("I want to test the repository")
                .benefit("So that I can ensure it works")
                .priority(UserStoryPriority.HIGH)
                .status(UserStoryStatus.TODO)
                .productBacklog(productBacklog)
                .acceptanceCriterias(List.of(acceptanceCriteria))
                .build();
    }

    @Test
    void existsByNameAndProductBacklogId_ShouldReturnTrue_WhenExists() {
        entityManager.persist(userStory);
        entityManager.flush();

        boolean exists = userStoryRepository.existsByNameAndProductBacklogId(
                userStory.getName(),
                productBacklog.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByNameAndProductBacklogId_ShouldReturnFalse_WhenDoesNotExist() {
        boolean exists = userStoryRepository.existsByNameAndProductBacklogId(
                "Non-existent Story",
                productBacklog.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistUserStory() {
        UserStory savedUserStory = userStoryRepository.save(userStory);

        assertThat(savedUserStory.getId()).isNotNull();
        assertThat(savedUserStory.getName()).isEqualTo(userStory.getName());
        assertThat(savedUserStory.getProductBacklog().getId()).isEqualTo(productBacklog.getId());
        assertThat(savedUserStory.getAcceptanceCriterias()).hasSize(1);
    }

    @Test
    void findById_ShouldReturnUserStory_WhenExists() {
        UserStory persistedUserStory = entityManager.persist(userStory);
        entityManager.flush();

        Optional<UserStory> found = userStoryRepository.findById(persistedUserStory.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(userStory.getName());
    }

    @Test
    void findAll_ShouldReturnAllUserStories() {
        entityManager.persist(userStory);

        AcceptanceCriteria secondAcceptanceCriteria = AcceptanceCriteria.builder()
                .given("Given another condition")
                .when("When another action occurs")
                .then("Then expect another result")
                .build();
        entityManager.persist(secondAcceptanceCriteria);

        UserStory secondUserStory = UserStory.builder()
                .name("Second User Story")
                .role("As a tester")
                .feature("I want to test findAll")
                .benefit("So that I can see all stories")
                .priority(UserStoryPriority.MEDIUM)
                .status(UserStoryStatus.TODO)
                .productBacklog(productBacklog)
                .acceptanceCriterias(List.of(secondAcceptanceCriteria))
                .build();
        entityManager.persist(secondUserStory);
        entityManager.flush();

        List<UserStory> userStories = userStoryRepository.findAll();

        assertThat(userStories).hasSize(2);
        assertThat(userStories).extracting(UserStory::getName)
                .containsExactlyInAnyOrder("Test User Story", "Second User Story");
        assertThat(userStories).allMatch(story -> !story.getAcceptanceCriterias().isEmpty());
    }

    @Test
    void delete_ShouldRemoveUserStory() {
        UserStory persistedUserStory = entityManager.persist(userStory);
        entityManager.flush();

        userStoryRepository.deleteById(persistedUserStory.getId());
        Optional<UserStory> found = userStoryRepository.findById(persistedUserStory.getId());

        assertThat(found).isEmpty();
    }
}