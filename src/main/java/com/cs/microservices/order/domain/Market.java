package com.cs.microservices.order.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Market.
 */
@Entity
@Table(name = "market")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Market implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "market_name")
    private String marketName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarketName() {
        return marketName;
    }

    public Market marketName(String marketName) {
        this.marketName = marketName;
        return this;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Market market = (Market) o;
        if (market.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), market.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Market{" +
            "id=" + getId() +
            ", marketName='" + getMarketName() + "'" +
            "}";
    }
}
