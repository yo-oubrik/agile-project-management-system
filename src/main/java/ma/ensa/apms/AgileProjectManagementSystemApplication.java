package ma.ensa.apms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AgileProjectManagementSystemApplication {

    // private final TaskService taskService;

    public static void main(String[] args) {
        SpringApplication.run(AgileProjectManagementSystemApplication.class, args);
    }

    // @Bean
    // public CommandLineRunner run() {
    // return args -> {
    // System.out.println("Creating task via TaskService...");
    // TaskRequestDto taskRequestDto = new TaskRequestDto();
    // taskRequestDto.setTitle("Task 1");
    // taskRequestDto.setDescription("Description 1");
    // taskRequestDto.setStartDate(LocalDateTime.now());
    // taskRequestDto.setEndDate(LocalDateTime.now().plusDays(7));
    // taskRequestDto.setStatus(TaskStatus.TODO);
    // taskService.createTask(taskRequestDto);
    // System.out.println("Task creation completed");
    // };
    // }
}