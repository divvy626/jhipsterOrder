package com.cs.microservices.order.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Execution entity.
 */
public class ExecutionDTO implements Serializable {

    private Long id;

    @NotNull
    private String executionId;

    @NotNull
    private Instant executionTime;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    private Long clientOrderId;

    private String clientOrderOrderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public Instant getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Instant executionTime) {
        this.executionTime = executionTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Long clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getClientOrderOrderId() {
        return clientOrderOrderId;
    }

    public void setClientOrderOrderId(String clientOrderOrderId) {
        this.clientOrderOrderId = clientOrderOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExecutionDTO executionDTO = (ExecutionDTO) o;
        if(executionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), executionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExecutionDTO{" +
            "id=" + getId() +
            ", executionId='" + getExecutionId() + "'" +
            ", executionTime='" + getExecutionTime() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
