package com.cs.microservices.order.service.mapper;

import com.cs.microservices.order.domain.*;
import com.cs.microservices.order.service.dto.ClientOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ClientOrder and its DTO ClientOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {StockMapper.class})
public interface ClientOrderMapper extends EntityMapper<ClientOrderDTO, ClientOrder> {

    @Mapping(source = "stock.id", target = "stockId")
    @Mapping(source = "stock.stockCode", target = "stockStockCode")
    ClientOrderDTO toDto(ClientOrder clientOrder);

    @Mapping(source = "stockId", target = "stock")
    @Mapping(target = "executions", ignore = true)
    ClientOrder toEntity(ClientOrderDTO clientOrderDTO);

    default ClientOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setId(id);
        return clientOrder;
    }
}
