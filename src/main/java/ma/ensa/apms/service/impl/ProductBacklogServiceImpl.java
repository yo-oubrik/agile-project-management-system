package ma.ensa.apms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import ma.ensa.apms.dto.ProductBacklogRequest;
import ma.ensa.apms.dto.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryDTO;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.ProductBacklogService;

@Service
@AllArgsConstructor
public class ProductBacklogServiceImpl implements ProductBacklogService {

    private UserStoryRepository userStoryRepository;
    private UserStoryMapper userStoryMapper;
    private ProductBacklogRepository productBacklogRepository;
    private ProductBacklogMapper productBacklogMapper;

    /**
     * Create a new product backlog
     * @param request the product backlog to create
     * @return the created product backlog
     */
    @Override
    @Transactional
    public ProductBacklogResponse create(ProductBacklogRequest request) {
        ProductBacklog pb = productBacklogMapper.toEntity(request);
        productBacklogRepository.save(pb);
        return productBacklogMapper.toResponse(pb);
    }

    /**
     * Return the list of user stories sorted by priority
     * @param productBacklog the product backlog to which the user stories belong
     * @return the list of user stories sorted by priority
     * @throws IllegalArgumentException if the product backlog is null
     * @throws ResourceNotFoundException if the product backlog is not found
     */
    @Override
    public List<UserStoryDTO> getBacklogUserStoriesSorted(Long productBacklogId) {
        if (productBacklogId == null) {
            throw new IllegalArgumentException("Product backlog is required");
        }
        productBacklogRepository.findById(productBacklogId)
            .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));
        return userStoryRepository.findByProductBacklogIdOrderByPriorityAsc(productBacklogId).stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a product backlog by id
     * @param id the id of the product backlog to get
     * @return the product backlog with the given id
     */
    @Override
    public ProductBacklogResponse getProductBacklogById(Long id) {
        ProductBacklog pb = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("backlog not found"));

        return productBacklogMapper.toResponse(pb);
    }

    @Override
    @Transactional
    public void deleteProductBacklog(Long productBacklogId) {
        productBacklogRepository.deleteById(productBacklogId);
    }
}
