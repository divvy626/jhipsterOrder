package com.cs.microservices.order.service.mapper;

import com.cs.microservices.order.domain.*;
import com.cs.microservices.order.service.dto.StockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Stock and its DTO StockDTO.
 */
@Mapper(componentModel = "spring", uses = {MarketMapper.class})
public interface StockMapper extends EntityMapper<StockDTO, Stock> {

    @Mapping(source = "market.id", target = "marketId")
    @Mapping(source = "market.marketName", target = "marketMarketName")
    StockDTO toDto(Stock stock);

    @Mapping(source = "marketId", target = "market")
    Stock toEntity(StockDTO stockDTO);

    default Stock fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stock stock = new Stock();
        stock.setId(id);
        return stock;
    }
}
