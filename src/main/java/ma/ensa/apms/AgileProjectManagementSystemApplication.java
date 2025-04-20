package ma.ensa.apms;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.repository.ProjectRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class AgileProjectManagementSystemApplication {

    private final ProjectRepository projectRepository;

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
        };
    }
}