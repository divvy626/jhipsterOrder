package com.cs.microservices.order.web.rest;

import com.cs.microservices.order.JhipsterOrderApp;

import com.cs.microservices.order.domain.ClientOrder;
import com.cs.microservices.order.domain.Stock;
import com.cs.microservices.order.repository.ClientOrderRepository;
import com.cs.microservices.order.service.ClientOrderService;
import com.cs.microservices.order.service.dto.ClientOrderDTO;
import com.cs.microservices.order.service.mapper.ClientOrderMapper;
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
 * Test class for the ClientOrderResource REST controller.
 *
 * @see ClientOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterOrderApp.class)
public class ClientOrderResourceIntTest {

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_ORDER_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ClientOrderRepository clientOrderRepository;

    @Autowired
    private ClientOrderMapper clientOrderMapper;

    @Autowired
    private ClientOrderService clientOrderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClientOrderMockMvc;

    private ClientOrder clientOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientOrderResource clientOrderResource = new ClientOrderResource(clientOrderService);
        this.restClientOrderMockMvc = MockMvcBuilders.standaloneSetup(clientOrderResource)
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
    public static ClientOrder createEntity(EntityManager em) {
        ClientOrder clientOrder = new ClientOrder()
            .orderId(DEFAULT_ORDER_ID)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .orderTime(DEFAULT_ORDER_TIME);
        // Add required entity
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        clientOrder.setStock(stock);
        return clientOrder;
    }

    @Before
    public void initTest() {
        clientOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientOrder() throws Exception {
        int databaseSizeBeforeCreate = clientOrderRepository.findAll().size();

        // Create the ClientOrder
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);
        restClientOrderMockMvc.perform(post("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the ClientOrder in the database
        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ClientOrder testClientOrder = clientOrderList.get(clientOrderList.size() - 1);
        assertThat(testClientOrder.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testClientOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testClientOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testClientOrder.getOrderTime()).isEqualTo(DEFAULT_ORDER_TIME);
    }

    @Test
    @Transactional
    public void createClientOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientOrderRepository.findAll().size();

        // Create the ClientOrder with an existing ID
        clientOrder.setId(1L);
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientOrderMockMvc.perform(post("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientOrder in the database
        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientOrderRepository.findAll().size();
        // set the field null
        clientOrder.setOrderId(null);

        // Create the ClientOrder, which fails.
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);

        restClientOrderMockMvc.perform(post("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isBadRequest());

        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientOrderRepository.findAll().size();
        // set the field null
        clientOrder.setPrice(null);

        // Create the ClientOrder, which fails.
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);

        restClientOrderMockMvc.perform(post("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isBadRequest());

        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientOrderRepository.findAll().size();
        // set the field null
        clientOrder.setQuantity(null);

        // Create the ClientOrder, which fails.
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);

        restClientOrderMockMvc.perform(post("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isBadRequest());

        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientOrderRepository.findAll().size();
        // set the field null
        clientOrder.setOrderTime(null);

        // Create the ClientOrder, which fails.
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);

        restClientOrderMockMvc.perform(post("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isBadRequest());

        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientOrders() throws Exception {
        // Initialize the database
        clientOrderRepository.saveAndFlush(clientOrder);

        // Get all the clientOrderList
        restClientOrderMockMvc.perform(get("/api/client-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].orderTime").value(hasItem(DEFAULT_ORDER_TIME.toString())));
    }

    @Test
    @Transactional
    public void getClientOrder() throws Exception {
        // Initialize the database
        clientOrderRepository.saveAndFlush(clientOrder);

        // Get the clientOrder
        restClientOrderMockMvc.perform(get("/api/client-orders/{id}", clientOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.orderTime").value(DEFAULT_ORDER_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClientOrder() throws Exception {
        // Get the clientOrder
        restClientOrderMockMvc.perform(get("/api/client-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientOrder() throws Exception {
        // Initialize the database
        clientOrderRepository.saveAndFlush(clientOrder);
        int databaseSizeBeforeUpdate = clientOrderRepository.findAll().size();

        // Update the clientOrder
        ClientOrder updatedClientOrder = clientOrderRepository.findOne(clientOrder.getId());
        // Disconnect from session so that the updates on updatedClientOrder are not directly saved in db
        em.detach(updatedClientOrder);
        updatedClientOrder
            .orderId(UPDATED_ORDER_ID)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .orderTime(UPDATED_ORDER_TIME);
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(updatedClientOrder);

        restClientOrderMockMvc.perform(put("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isOk());

        // Validate the ClientOrder in the database
        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeUpdate);
        ClientOrder testClientOrder = clientOrderList.get(clientOrderList.size() - 1);
        assertThat(testClientOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testClientOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testClientOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testClientOrder.getOrderTime()).isEqualTo(UPDATED_ORDER_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingClientOrder() throws Exception {
        int databaseSizeBeforeUpdate = clientOrderRepository.findAll().size();

        // Create the ClientOrder
        ClientOrderDTO clientOrderDTO = clientOrderMapper.toDto(clientOrder);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClientOrderMockMvc.perform(put("/api/client-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the ClientOrder in the database
        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClientOrder() throws Exception {
        // Initialize the database
        clientOrderRepository.saveAndFlush(clientOrder);
        int databaseSizeBeforeDelete = clientOrderRepository.findAll().size();

        // Get the clientOrder
        restClientOrderMockMvc.perform(delete("/api/client-orders/{id}", clientOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientOrder> clientOrderList = clientOrderRepository.findAll();
        assertThat(clientOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientOrder.class);
        ClientOrder clientOrder1 = new ClientOrder();
        clientOrder1.setId(1L);
        ClientOrder clientOrder2 = new ClientOrder();
        clientOrder2.setId(clientOrder1.getId());
        assertThat(clientOrder1).isEqualTo(clientOrder2);
        clientOrder2.setId(2L);
        assertThat(clientOrder1).isNotEqualTo(clientOrder2);
        clientOrder1.setId(null);
        assertThat(clientOrder1).isNotEqualTo(clientOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientOrderDTO.class);
        ClientOrderDTO clientOrderDTO1 = new ClientOrderDTO();
        clientOrderDTO1.setId(1L);
        ClientOrderDTO clientOrderDTO2 = new ClientOrderDTO();
        assertThat(clientOrderDTO1).isNotEqualTo(clientOrderDTO2);
        clientOrderDTO2.setId(clientOrderDTO1.getId());
        assertThat(clientOrderDTO1).isEqualTo(clientOrderDTO2);
        clientOrderDTO2.setId(2L);
        assertThat(clientOrderDTO1).isNotEqualTo(clientOrderDTO2);
        clientOrderDTO1.setId(null);
        assertThat(clientOrderDTO1).isNotEqualTo(clientOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clientOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clientOrderMapper.fromId(null)).isNull();
    }
}
