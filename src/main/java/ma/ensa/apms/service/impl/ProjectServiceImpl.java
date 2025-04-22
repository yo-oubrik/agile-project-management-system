package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.ProjectRepository;
import ma.ensa.apms.service.ProjectService;
import ma.ensa.apms.mapper.ProjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProductBacklogRepository productBacklogRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Project project = projectMapper.toEntity(request);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse updateProject(UUID id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        projectMapper.updateEntityFromRequest(request, project);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectResponse updateProjectStartDate(UUID id, LocalDateTime startDate) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        project.setStartDate(startDate);
        if (project.getEndDate() != null && project.getEndDate().isBefore(startDate)) {
            throw new BusinessException("Start date must be before end date");
        }
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse updateProjectEndDate(UUID id, LocalDateTime endDate) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        project.setEndDate(endDate);
        if (project.getStartDate() != null && project.getStartDate().isAfter(endDate)) {
            throw new BusinessException("End date must be after start date");
        }
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse updateProjectStatus(UUID id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        project.setStatus(status);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse getProject(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponse> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponse> getProjectsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return projectRepository.findByStartDateAfterAndEndDateBefore(startDate, endDate)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectResponse assignProductBacklogToProject(UUID projectId, UUID productBacklogId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (project.getProductBacklog() != null) {
            throw new IllegalStateException("This project already has a ProductBacklog assigned");
        }

        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        project.setProductBacklog(productBacklog);
        projectRepository.save(project);

        return projectMapper.toResponse(project);
    }
}
