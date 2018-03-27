package com.cs.microservices.order.service.mapper;

import com.cs.microservices.order.domain.*;
import com.cs.microservices.order.service.dto.ExecutionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Execution and its DTO ExecutionDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientOrderMapper.class})
public interface ExecutionMapper extends EntityMapper<ExecutionDTO, Execution> {

    @Mapping(source = "clientOrder.id", target = "clientOrderId")
    @Mapping(source = "clientOrder.orderId", target = "clientOrderOrderId")
    ExecutionDTO toDto(Execution execution);

    @Mapping(source = "clientOrderId", target = "clientOrder")
    Execution toEntity(ExecutionDTO executionDTO);

    default Execution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Execution execution = new Execution();
        execution.setId(id);
        return execution;
    }
}
