package com.cs.microservices.order.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ClientOrder entity.
 */
public class ClientOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private String orderId;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    @NotNull
    private Instant orderTime;

    private Long stockId;

    private String stockStockCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Instant getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Instant orderTime) {
        this.orderTime = orderTime;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockStockCode() {
        return stockStockCode;
    }

    public void setStockStockCode(String stockStockCode) {
        this.stockStockCode = stockStockCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientOrderDTO clientOrderDTO = (ClientOrderDTO) o;
        if(clientOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientOrderDTO{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", orderTime='" + getOrderTime() + "'" +
            "}";
    }
}
