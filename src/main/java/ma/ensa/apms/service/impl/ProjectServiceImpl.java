package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.repository.ProjectRepository;
import ma.ensa.apms.service.ProjectService;
import ma.ensa.apms.mapper.ProjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
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
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse updateProjectEndDate(UUID id, LocalDateTime endDate) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        project.setEndDate(endDate);
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
}
