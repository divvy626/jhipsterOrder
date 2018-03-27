package com.cs.microservices.order.service.impl;

import com.cs.microservices.order.service.ExecutionService;
import com.cs.microservices.order.domain.Execution;
import com.cs.microservices.order.repository.ExecutionRepository;
import com.cs.microservices.order.service.dto.ExecutionDTO;
import com.cs.microservices.order.service.mapper.ExecutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Execution.
 */
@Service
@Transactional
public class ExecutionServiceImpl implements ExecutionService {

    private final Logger log = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    private final ExecutionRepository executionRepository;

    private final ExecutionMapper executionMapper;

    public ExecutionServiceImpl(ExecutionRepository executionRepository, ExecutionMapper executionMapper) {
        this.executionRepository = executionRepository;
        this.executionMapper = executionMapper;
    }

    /**
     * Save a execution.
     *
     * @param executionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExecutionDTO save(ExecutionDTO executionDTO) {
        log.debug("Request to save Execution : {}", executionDTO);
        Execution execution = executionMapper.toEntity(executionDTO);
        execution = executionRepository.save(execution);
        return executionMapper.toDto(execution);
    }

    /**
     * Get all the executions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExecutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Executions");
        return executionRepository.findAll(pageable)
            .map(executionMapper::toDto);
    }

    /**
     * Get one execution by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExecutionDTO findOne(Long id) {
        log.debug("Request to get Execution : {}", id);
        Execution execution = executionRepository.findOne(id);
        return executionMapper.toDto(execution);
    }

    /**
     * Delete the execution by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Execution : {}", id);
        executionRepository.delete(id);
    }
}
