package com.cs.microservices.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs.microservices.order.service.ExecutionService;
import com.cs.microservices.order.web.rest.errors.BadRequestAlertException;
import com.cs.microservices.order.web.rest.util.HeaderUtil;
import com.cs.microservices.order.web.rest.util.PaginationUtil;
import com.cs.microservices.order.service.dto.ExecutionDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Execution.
 */
@RestController
@RequestMapping("/api")
public class ExecutionResource {

    private final Logger log = LoggerFactory.getLogger(ExecutionResource.class);

    private static final String ENTITY_NAME = "execution";

    private final ExecutionService executionService;

    public ExecutionResource(ExecutionService executionService) {
        this.executionService = executionService;
    }

    /**
     * POST  /executions : Create a new execution.
     *
     * @param executionDTO the executionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new executionDTO, or with status 400 (Bad Request) if the execution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/executions")
    @Timed
    public ResponseEntity<ExecutionDTO> createExecution(@Valid @RequestBody ExecutionDTO executionDTO) throws URISyntaxException {
        log.debug("REST request to save Execution : {}", executionDTO);
        if (executionDTO.getId() != null) {
            throw new BadRequestAlertException("A new execution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExecutionDTO result = executionService.save(executionDTO);
        return ResponseEntity.created(new URI("/api/executions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /executions : Updates an existing execution.
     *
     * @param executionDTO the executionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated executionDTO,
     * or with status 400 (Bad Request) if the executionDTO is not valid,
     * or with status 500 (Internal Server Error) if the executionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/executions")
    @Timed
    public ResponseEntity<ExecutionDTO> updateExecution(@Valid @RequestBody ExecutionDTO executionDTO) throws URISyntaxException {
        log.debug("REST request to update Execution : {}", executionDTO);
        if (executionDTO.getId() == null) {
            return createExecution(executionDTO);
        }
        ExecutionDTO result = executionService.save(executionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, executionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /executions : get all the executions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of executions in body
     */
    @GetMapping("/executions")
    @Timed
    public ResponseEntity<List<ExecutionDTO>> getAllExecutions(Pageable pageable) {
        log.debug("REST request to get a page of Executions");
        Page<ExecutionDTO> page = executionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/executions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /executions/:id : get the "id" execution.
     *
     * @param id the id of the executionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the executionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/executions/{id}")
    @Timed
    public ResponseEntity<ExecutionDTO> getExecution(@PathVariable Long id) {
        log.debug("REST request to get Execution : {}", id);
        ExecutionDTO executionDTO = executionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(executionDTO));
    }

    /**
     * DELETE  /executions/:id : delete the "id" execution.
     *
     * @param id the id of the executionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/executions/{id}")
    @Timed
    public ResponseEntity<Void> deleteExecution(@PathVariable Long id) {
        log.debug("REST request to delete Execution : {}", id);
        executionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
