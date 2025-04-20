package ma.ensa.apms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.modal.enums.ProjectStatus;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ma.ensa.apms.service.ProjectService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse createProject(@RequestBody ProjectRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping("/status/{status}")
    public List<ProjectResponse> getProjectsByStatus(@PathVariable ProjectStatus status) {
        return projectService.getProjectsByStatus(status);
    }

    @GetMapping("/range")
    public List<ProjectResponse> getProjectsBetweenDates(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return projectService.getProjectsBetweenDates(startDate, endDate);
    }

    @PatchMapping("/{id}/startDate")
    public ProjectResponse updateProjectStartDate(@PathVariable UUID id, @RequestBody LocalDateTime startDate) {
        return projectService.updateProjectStartDate(id, startDate);
    }

    @PatchMapping("/{id}/endDate")
    public ProjectResponse updateProjectEndDate(@PathVariable UUID id, @RequestBody LocalDateTime endDate) {
        return projectService.updateProjectEndDate(id, endDate);
    }

    @PatchMapping("/{id}/status")
    public ProjectResponse updateProjectStatus(@PathVariable UUID id, @RequestBody ProjectStatus status) {
        return projectService.updateProjectStatus(id, status);
    }

    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectService.getAllProjects(pageable);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectRequest request) {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }
}
