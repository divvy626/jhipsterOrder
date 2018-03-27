package com.cs.microservices.order.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Market entity.
 */
public class MarketDTO implements Serializable {

    private Long id;

    private String marketName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MarketDTO marketDTO = (MarketDTO) o;
        if(marketDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), marketDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MarketDTO{" +
            "id=" + getId() +
            ", marketName='" + getMarketName() + "'" +
            "}";
    }
}
