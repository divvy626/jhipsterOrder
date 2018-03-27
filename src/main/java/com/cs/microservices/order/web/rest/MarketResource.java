package com.cs.microservices.order.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs.microservices.order.service.MarketService;
import com.cs.microservices.order.web.rest.errors.BadRequestAlertException;
import com.cs.microservices.order.web.rest.util.HeaderUtil;
import com.cs.microservices.order.web.rest.util.PaginationUtil;
import com.cs.microservices.order.service.dto.MarketDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Market.
 */
@RestController
@RequestMapping("/api")
public class MarketResource {

    private final Logger log = LoggerFactory.getLogger(MarketResource.class);

    private static final String ENTITY_NAME = "market";

    private final MarketService marketService;

    public MarketResource(MarketService marketService) {
        this.marketService = marketService;
    }

    /**
     * POST  /markets : Create a new market.
     *
     * @param marketDTO the marketDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marketDTO, or with status 400 (Bad Request) if the market has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/markets")
    @Timed
    public ResponseEntity<MarketDTO> createMarket(@RequestBody MarketDTO marketDTO) throws URISyntaxException {
        log.debug("REST request to save Market : {}", marketDTO);
        if (marketDTO.getId() != null) {
            throw new BadRequestAlertException("A new market cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarketDTO result = marketService.save(marketDTO);
        return ResponseEntity.created(new URI("/api/markets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /markets : Updates an existing market.
     *
     * @param marketDTO the marketDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marketDTO,
     * or with status 400 (Bad Request) if the marketDTO is not valid,
     * or with status 500 (Internal Server Error) if the marketDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/markets")
    @Timed
    public ResponseEntity<MarketDTO> updateMarket(@RequestBody MarketDTO marketDTO) throws URISyntaxException {
        log.debug("REST request to update Market : {}", marketDTO);
        if (marketDTO.getId() == null) {
            return createMarket(marketDTO);
        }
        MarketDTO result = marketService.save(marketDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marketDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /markets : get all the markets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of markets in body
     */
    @GetMapping("/markets")
    @Timed
    public ResponseEntity<List<MarketDTO>> getAllMarkets(Pageable pageable) {
        log.debug("REST request to get a page of Markets");
        Page<MarketDTO> page = marketService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/markets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /markets/:id : get the "id" market.
     *
     * @param id the id of the marketDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marketDTO, or with status 404 (Not Found)
     */
    @GetMapping("/markets/{id}")
    @Timed
    public ResponseEntity<MarketDTO> getMarket(@PathVariable Long id) {
        log.debug("REST request to get Market : {}", id);
        MarketDTO marketDTO = marketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(marketDTO));
    }

    /**
     * DELETE  /markets/:id : delete the "id" market.
     *
     * @param id the id of the marketDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/markets/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {
        log.debug("REST request to delete Market : {}", id);
        marketService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
