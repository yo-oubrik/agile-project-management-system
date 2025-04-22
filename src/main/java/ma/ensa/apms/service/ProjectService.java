package ma.ensa.apms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.modal.enums.ProjectStatus;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(UUID id, ProjectRequest request);

    void deleteProject(UUID id);

    ProjectResponse updateProjectStartDate(UUID id, LocalDateTime startDate);

    ProjectResponse updateProjectEndDate(UUID id, LocalDateTime endDate);

    ProjectResponse updateProjectStatus(UUID id, ProjectStatus status);

    ProjectResponse getProject(UUID id);

    List<ProjectResponse> getAllProjects(Pageable pageable);

    List<ProjectResponse> getProjectsByStatus(ProjectStatus status);

    List<ProjectResponse> getProjectsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    ProjectResponse assignProductBacklogToProject(UUID projectId, UUID productBacklogId);

}
