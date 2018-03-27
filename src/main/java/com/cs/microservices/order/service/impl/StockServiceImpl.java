package com.cs.microservices.order.service.impl;

import com.cs.microservices.order.service.StockService;
import com.cs.microservices.order.domain.Stock;
import com.cs.microservices.order.repository.StockRepository;
import com.cs.microservices.order.service.dto.StockDTO;
import com.cs.microservices.order.service.mapper.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;

    private final StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockDTO save(StockDTO stockDTO) {
        log.debug("Request to save Stock : {}", stockDTO);
        Stock stock = stockMapper.toEntity(stockDTO);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable)
            .map(stockMapper::toDto);
    }

    /**
     * Get one stock by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockDTO findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        Stock stock = stockRepository.findOne(id);
        return stockMapper.toDto(stock);
    }

    /**
     * Delete the stock by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.delete(id);
    }
}
