package com.cs.microservices.order.service;

import com.cs.microservices.order.service.dto.ClientOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ClientOrder.
 */
public interface ClientOrderService {

    /**
     * Save a clientOrder.
     *
     * @param clientOrderDTO the entity to save
     * @return the persisted entity
     */
    ClientOrderDTO save(ClientOrderDTO clientOrderDTO);

    /**
     * Get all the clientOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ClientOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" clientOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ClientOrderDTO findOne(Long id);

    /**
     * Delete the "id" clientOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
