package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Request.ProductBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.ProjectMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.ProjectRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.ProductBacklogService;

@Service
@RequiredArgsConstructor
public class ProductBacklogServiceImpl implements ProductBacklogService {

    private final UserStoryRepository userStoryRepository;
    private final ProjectRepository projectRepository;
    private final UserStoryMapper userStoryMapper;
    private final ProductBacklogRepository productBacklogRepository;
    private final ProductBacklogMapper productBacklogMapper;
    private final EpicMapper epicMapper;
    private final EpicRepository epicRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProductBacklogResponse create(ProductBacklogRequest request) {
        ProductBacklog pb = productBacklogMapper.toEntity(request);
        productBacklogRepository.save(pb);
        return productBacklogMapper.toResponse(pb);
    }

    @Override
    public ProductBacklogResponse getProductBacklogById(UUID id) {
        ProductBacklog pb = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("backlog not found"));

        ProductBacklogResponse response = productBacklogMapper.toResponse(pb);

        setCounts(response, pb);

        return response;
    }

    @Override
    @Transactional
    public void deleteProductBacklog(UUID productBacklogId) {
        if (!productBacklogRepository.existsById(productBacklogId)) {
            throw new ResourceNotFoundException("Product backlog with ID " + productBacklogId + " does not exist");
        }
        productBacklogRepository.deleteById(productBacklogId);
    }

    @Override
    public List<ProductBacklogResponse> getAllProductBacklogs() {
        return productBacklogRepository.findAll().stream()
                .map(productBacklogMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserStoryResponse> getUserStoriesByProductBacklogId(UUID productBacklogId) {
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        return userStoryRepository.findByProductBacklog(productBacklog).stream()
                .map(userStoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EpicResponse> getEpicsByProductBacklogId(UUID productBacklogId) {
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        return productBacklog.getEpics().stream()
                .map(epicMapper::toDto)
                .collect(Collectors.toList());
    }

    private void setCounts(ProductBacklogResponse response, ProductBacklog pb) {
        response.setUserStoryCount(pb.getUserStories() != null ? pb.getUserStories().size() : 0);
        response.setEpicCount(pb.getEpics() != null ? pb.getEpics().size() : 0);
    }

    @Override
    @Transactional
    public EpicResponse addEpicToProductBacklog(UUID productBacklogId, EpicRequest epicRequest) {
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        Epic epic = epicMapper.toEntity(epicRequest);
        epic.setProductBacklog(productBacklog);
        Epic savedEpic = epicRepository.save(epic);
        productBacklog.getEpics().add(savedEpic);

        productBacklogRepository.save(productBacklog);
        return epicMapper.toDto(savedEpic);
    }

    @Override
    @Transactional
    public UserStoryResponse addUserStoryToProductBacklog(UUID productBacklogId, UserStoryRequest userStoryRequest) {
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        UserStory userStory = userStoryMapper.toEntity(userStoryRequest);
        userStory.setProductBacklog(productBacklog);
        productBacklog.getUserStories().add(userStory);

        productBacklogRepository.save(productBacklog);
        return userStoryMapper.toResponse(userStory);
    }

    @Override
    @Transactional
    public ProductBacklogResponse assignProjectToProductBacklog(UUID productBacklogId, UUID projectId) {
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        productBacklog.setProject(project);
        productBacklogRepository.save(productBacklog);

        return productBacklogMapper.toResponse(productBacklog);
    }

    @Override
    @Transactional
    public ProjectResponse getProjectByProductBacklogId(UUID productBacklogId) {
        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        Project project = productBacklog.getProject();
        if (project == null) {
            throw new ResourceNotFoundException("No project associated with this product backlog");
        }

        return projectMapper.toResponse(project);
    }
}
