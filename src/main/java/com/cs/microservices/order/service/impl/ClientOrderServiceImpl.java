package com.cs.microservices.order.service.impl;

import com.cs.microservices.order.service.ClientOrderService;
import com.cs.microservices.order.domain.ClientOrder;
import com.cs.microservices.order.repository.ClientOrderRepository;
import com.cs.microservices.order.service.dto.ClientOrderDTO;
import com.cs.microservices.order.service.mapper.ClientOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ClientOrder.
 */
@Service
@Transactional
public class ClientOrderServiceImpl implements ClientOrderService {

    private final Logger log = LoggerFactory.getLogger(ClientOrderServiceImpl.class);

    private final ClientOrderRepository clientOrderRepository;

    private final ClientOrderMapper clientOrderMapper;

    public ClientOrderServiceImpl(ClientOrderRepository clientOrderRepository, ClientOrderMapper clientOrderMapper) {
        this.clientOrderRepository = clientOrderRepository;
        this.clientOrderMapper = clientOrderMapper;
    }

    /**
     * Save a clientOrder.
     *
     * @param clientOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClientOrderDTO save(ClientOrderDTO clientOrderDTO) {
        log.debug("Request to save ClientOrder : {}", clientOrderDTO);
        ClientOrder clientOrder = clientOrderMapper.toEntity(clientOrderDTO);
        clientOrder = clientOrderRepository.save(clientOrder);
        return clientOrderMapper.toDto(clientOrder);
    }

    /**
     * Get all the clientOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClientOrders");
        return clientOrderRepository.findAll(pageable)
            .map(clientOrderMapper::toDto);
    }

    /**
     * Get one clientOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ClientOrderDTO findOne(Long id) {
        log.debug("Request to get ClientOrder : {}", id);
        ClientOrder clientOrder = clientOrderRepository.findOne(id);
        return clientOrderMapper.toDto(clientOrder);
    }

    /**
     * Delete the clientOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClientOrder : {}", id);
        clientOrderRepository.delete(id);
    }
}
