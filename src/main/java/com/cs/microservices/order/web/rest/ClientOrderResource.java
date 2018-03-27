package com.cs.microservices.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs.microservices.order.service.ClientOrderService;
import com.cs.microservices.order.web.rest.errors.BadRequestAlertException;
import com.cs.microservices.order.web.rest.util.HeaderUtil;
import com.cs.microservices.order.web.rest.util.PaginationUtil;
import com.cs.microservices.order.service.dto.ClientOrderDTO;
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
 * REST controller for managing ClientOrder.
 */
@RestController
@RequestMapping("/api")
public class ClientOrderResource {

    private final Logger log = LoggerFactory.getLogger(ClientOrderResource.class);

    private static final String ENTITY_NAME = "clientOrder";

    private final ClientOrderService clientOrderService;

    public ClientOrderResource(ClientOrderService clientOrderService) {
        this.clientOrderService = clientOrderService;
    }

    /**
     * POST  /client-orders : Create a new clientOrder.
     *
     * @param clientOrderDTO the clientOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientOrderDTO, or with status 400 (Bad Request) if the clientOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/client-orders")
    @Timed
    public ResponseEntity<ClientOrderDTO> createClientOrder(@Valid @RequestBody ClientOrderDTO clientOrderDTO) throws URISyntaxException {
        log.debug("REST request to save ClientOrder : {}", clientOrderDTO);
        if (clientOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientOrderDTO result = clientOrderService.save(clientOrderDTO);
        return ResponseEntity.created(new URI("/api/client-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-orders : Updates an existing clientOrder.
     *
     * @param clientOrderDTO the clientOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientOrderDTO,
     * or with status 400 (Bad Request) if the clientOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/client-orders")
    @Timed
    public ResponseEntity<ClientOrderDTO> updateClientOrder(@Valid @RequestBody ClientOrderDTO clientOrderDTO) throws URISyntaxException {
        log.debug("REST request to update ClientOrder : {}", clientOrderDTO);
        if (clientOrderDTO.getId() == null) {
            return createClientOrder(clientOrderDTO);
        }
        ClientOrderDTO result = clientOrderService.save(clientOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clientOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-orders : get all the clientOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientOrders in body
     */
    @GetMapping("/client-orders")
    @Timed
    public ResponseEntity<List<ClientOrderDTO>> getAllClientOrders(Pageable pageable) {
        log.debug("REST request to get a page of ClientOrders");
        Page<ClientOrderDTO> page = clientOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-orders/:id : get the "id" clientOrder.
     *
     * @param id the id of the clientOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/client-orders/{id}")
    @Timed
    public ResponseEntity<ClientOrderDTO> getClientOrder(@PathVariable Long id) {
        log.debug("REST request to get ClientOrder : {}", id);
        ClientOrderDTO clientOrderDTO = clientOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clientOrderDTO));
    }

    /**
     * DELETE  /client-orders/:id : delete the "id" clientOrder.
     *
     * @param id the id of the clientOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/client-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteClientOrder(@PathVariable Long id) {
        log.debug("REST request to delete ClientOrder : {}", id);
        clientOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
