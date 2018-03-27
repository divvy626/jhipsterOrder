package com.cs.microservices.order.web.rest;

import com.cs.microservices.order.JhipsterOrderApp;

import com.cs.microservices.order.domain.Execution;
import com.cs.microservices.order.domain.ClientOrder;
import com.cs.microservices.order.repository.ExecutionRepository;
import com.cs.microservices.order.service.ExecutionService;
import com.cs.microservices.order.service.dto.ExecutionDTO;
import com.cs.microservices.order.service.mapper.ExecutionMapper;
import com.cs.microservices.order.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.cs.microservices.order.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExecutionResource REST controller.
 *
 * @see ExecutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterOrderApp.class)
public class ExecutionResourceIntTest {

    private static final String DEFAULT_EXECUTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXECUTION_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXECUTION_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXECUTION_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private ExecutionMapper executionMapper;

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExecutionMockMvc;

    private Execution execution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExecutionResource executionResource = new ExecutionResource(executionService);
        this.restExecutionMockMvc = MockMvcBuilders.standaloneSetup(executionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Execution createEntity(EntityManager em) {
        Execution execution = new Execution()
            .executionId(DEFAULT_EXECUTION_ID)
            .executionTime(DEFAULT_EXECUTION_TIME)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        ClientOrder clientOrder = ClientOrderResourceIntTest.createEntity(em);
        em.persist(clientOrder);
        em.flush();
        execution.setClientOrder(clientOrder);
        return execution;
    }

    @Before
    public void initTest() {
        execution = createEntity(em);
    }

    @Test
    @Transactional
    public void createExecution() throws Exception {
        int databaseSizeBeforeCreate = executionRepository.findAll().size();

        // Create the Execution
        ExecutionDTO executionDTO = executionMapper.toDto(execution);
        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isCreated());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeCreate + 1);
        Execution testExecution = executionList.get(executionList.size() - 1);
        assertThat(testExecution.getExecutionId()).isEqualTo(DEFAULT_EXECUTION_ID);
        assertThat(testExecution.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
        assertThat(testExecution.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testExecution.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createExecutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = executionRepository.findAll().size();

        // Create the Execution with an existing ID
        execution.setId(1L);
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkExecutionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionRepository.findAll().size();
        // set the field null
        execution.setExecutionId(null);

        // Create the Execution, which fails.
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isBadRequest());

        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExecutionTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionRepository.findAll().size();
        // set the field null
        execution.setExecutionTime(null);

        // Create the Execution, which fails.
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isBadRequest());

        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionRepository.findAll().size();
        // set the field null
        execution.setPrice(null);

        // Create the Execution, which fails.
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isBadRequest());

        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionRepository.findAll().size();
        // set the field null
        execution.setQuantity(null);

        // Create the Execution, which fails.
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        restExecutionMockMvc.perform(post("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isBadRequest());

        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExecutions() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        // Get all the executionList
        restExecutionMockMvc.perform(get("/api/executions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(execution.getId().intValue())))
            .andExpect(jsonPath("$.[*].executionId").value(hasItem(DEFAULT_EXECUTION_ID.toString())))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);

        // Get the execution
        restExecutionMockMvc.perform(get("/api/executions/{id}", execution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(execution.getId().intValue()))
            .andExpect(jsonPath("$.executionId").value(DEFAULT_EXECUTION_ID.toString()))
            .andExpect(jsonPath("$.executionTime").value(DEFAULT_EXECUTION_TIME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingExecution() throws Exception {
        // Get the execution
        restExecutionMockMvc.perform(get("/api/executions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);
        int databaseSizeBeforeUpdate = executionRepository.findAll().size();

        // Update the execution
        Execution updatedExecution = executionRepository.findOne(execution.getId());
        // Disconnect from session so that the updates on updatedExecution are not directly saved in db
        em.detach(updatedExecution);
        updatedExecution
            .executionId(UPDATED_EXECUTION_ID)
            .executionTime(UPDATED_EXECUTION_TIME)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY);
        ExecutionDTO executionDTO = executionMapper.toDto(updatedExecution);

        restExecutionMockMvc.perform(put("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isOk());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeUpdate);
        Execution testExecution = executionList.get(executionList.size() - 1);
        assertThat(testExecution.getExecutionId()).isEqualTo(UPDATED_EXECUTION_ID);
        assertThat(testExecution.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testExecution.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testExecution.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingExecution() throws Exception {
        int databaseSizeBeforeUpdate = executionRepository.findAll().size();

        // Create the Execution
        ExecutionDTO executionDTO = executionMapper.toDto(execution);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExecutionMockMvc.perform(put("/api/executions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionDTO)))
            .andExpect(status().isCreated());

        // Validate the Execution in the database
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExecution() throws Exception {
        // Initialize the database
        executionRepository.saveAndFlush(execution);
        int databaseSizeBeforeDelete = executionRepository.findAll().size();

        // Get the execution
        restExecutionMockMvc.perform(delete("/api/executions/{id}", execution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Execution> executionList = executionRepository.findAll();
        assertThat(executionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Execution.class);
        Execution execution1 = new Execution();
        execution1.setId(1L);
        Execution execution2 = new Execution();
        execution2.setId(execution1.getId());
        assertThat(execution1).isEqualTo(execution2);
        execution2.setId(2L);
        assertThat(execution1).isNotEqualTo(execution2);
        execution1.setId(null);
        assertThat(execution1).isNotEqualTo(execution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionDTO.class);
        ExecutionDTO executionDTO1 = new ExecutionDTO();
        executionDTO1.setId(1L);
        ExecutionDTO executionDTO2 = new ExecutionDTO();
        assertThat(executionDTO1).isNotEqualTo(executionDTO2);
        executionDTO2.setId(executionDTO1.getId());
        assertThat(executionDTO1).isEqualTo(executionDTO2);
        executionDTO2.setId(2L);
        assertThat(executionDTO1).isNotEqualTo(executionDTO2);
        executionDTO1.setId(null);
        assertThat(executionDTO1).isNotEqualTo(executionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(executionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(executionMapper.fromId(null)).isNull();
    }
}
