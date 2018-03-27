package com.cs.microservices.order.service;

import com.cs.microservices.order.service.dto.MarketDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Market.
 */
public interface MarketService {

    /**
     * Save a market.
     *
     * @param marketDTO the entity to save
     * @return the persisted entity
     */
    MarketDTO save(MarketDTO marketDTO);

    /**
     * Get all the markets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MarketDTO> findAll(Pageable pageable);

    /**
     * Get the "id" market.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MarketDTO findOne(Long id);

    /**
     * Delete the "id" market.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
