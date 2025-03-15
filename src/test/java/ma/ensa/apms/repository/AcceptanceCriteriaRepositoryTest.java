package ma.ensa.apms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

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
class AcceptanceCriteriaRepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private AcceptanceCriteriaRepository repository;

        private UserStory userStory;
        private AcceptanceCriteria acceptanceCriteria1;
        private AcceptanceCriteria acceptanceCriteria2;
        private ProductBacklog productBacklog;

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

                acceptanceCriteria1 = AcceptanceCriteria.builder()
                                .given("Given a test condition")
                                .when("When an action occurs")
                                .then("Then expect a result")
                                .userStory(userStory)
                                .build();
                acceptanceCriteria1 = entityManager.persist(acceptanceCriteria1);

                userStory.getAcceptanceCriterias().add(acceptanceCriteria1);

                acceptanceCriteria2 = AcceptanceCriteria.builder()
                                .given("Given another condition")
                                .when("When something else happens")
                                .then("Then expect another result")
                                .userStory(userStory)
                                .build();
                acceptanceCriteria2 = entityManager.persist(acceptanceCriteria2);

                userStory.getAcceptanceCriterias().add(acceptanceCriteria2);

                entityManager.flush();
        }

        @Test
        void save_ShouldPersistAcceptanceCriteria() {
                AcceptanceCriteria newCriteria = AcceptanceCriteria.builder()
                                .given("Given a new condition")
                                .when("When something new happens")
                                .then("Then expect new result")
                                .build();

                AcceptanceCriteria saved = repository.save(newCriteria);

                assertThat(saved.getId()).isNotNull();
                assertThat(saved.getGiven()).isEqualTo(newCriteria.getGiven());
                assertThat(saved.getWhen()).isEqualTo(newCriteria.getWhen());
                assertThat(saved.getThen()).isEqualTo(newCriteria.getThen());
        }

        @Test
        void findById_ShouldReturnAcceptanceCriteria() {

                AcceptanceCriteria found = repository.findById(acceptanceCriteria1.getId())
                                .orElse(null);

                assertThat(found).isNotNull();
                assertThat(found.getId()).isEqualTo(acceptanceCriteria1.getId());
                assertThat(found.getGiven()).isEqualTo(acceptanceCriteria1.getGiven());
        }
}