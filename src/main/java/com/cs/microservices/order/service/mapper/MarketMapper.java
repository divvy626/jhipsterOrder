package com.cs.microservices.order.service.mapper;

import com.cs.microservices.order.domain.*;
import com.cs.microservices.order.service.dto.MarketDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Market and its DTO MarketDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MarketMapper extends EntityMapper<MarketDTO, Market> {



    default Market fromId(Long id) {
        if (id == null) {
            return null;
        }
        Market market = new Market();
        market.setId(id);
        return market;
    }
}
