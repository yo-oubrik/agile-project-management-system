package ma.ensa.apms;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProjectRepository;
import ma.ensa.apms.repository.UserStoryRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class AgileProjectManagementSystemApplication {

    private final ProjectRepository projectRepository;
    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;

    public static void main(String[] args) {
        SpringApplication.run(AgileProjectManagementSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(){
        return args -> {
            Project project = projectRepository.save(
                Project.builder()
                    .name("Project 1")
                    .description("Description 1")
                    .status(ProjectStatus.NOT_STARTED)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusMonths(1))
                    .build()
            );
            System.out.println("Project created: " + project);

            UserStory userStory = userStoryRepository.save(
                UserStory.builder()
                    .name("User Story 1")
                    .role("Role 1")
                    .feature("Feature 1")
                    .benefit("Benefit 1")
                    .status(UserStoryStatus.TODO)
                    .build()
            );
            
            List<AcceptanceCriteria> acceptanceCriterias = List.of(
                AcceptanceCriteria.builder()
                .given("Given 1")
                .when("When 1")
                    .then("Then 1")
                    .met(false)
                    .userStory(userStory)
                    .build(),

                AcceptanceCriteria.builder()
                .given("Given 2")
                .when("When 2")
                .then("Then 2")
                .met(false)
                .userStory(userStory)
                .build()
                );
                userStory.setAcceptanceCriterias(acceptanceCriterias);
                userStoryRepository.save(userStory);

            epicRepository.save(
                Epic.builder()
                    .name("Epic 1")
                    .description("Description 1")
                    .build()
            );
                
            System.out.println("User Story created: " + userStory);
        };
    }
}