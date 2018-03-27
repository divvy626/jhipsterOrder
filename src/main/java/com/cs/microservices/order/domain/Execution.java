package com.cs.microservices.order.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Execution.
 */
@Entity
@Table(name = "execution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Execution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "execution_id", nullable = false)
    private String executionId;

    @NotNull
    @Column(name = "execution_time", nullable = false)
    private Instant executionTime;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    private ClientOrder clientOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutionId() {
        return executionId;
    }

    public Execution executionId(String executionId) {
        this.executionId = executionId;
        return this;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public Instant getExecutionTime() {
        return executionTime;
    }

    public Execution executionTime(Instant executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public void setExecutionTime(Instant executionTime) {
        this.executionTime = executionTime;
    }

    public Double getPrice() {
        return price;
    }

    public Execution price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Execution quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ClientOrder getClientOrder() {
        return clientOrder;
    }

    public Execution clientOrder(ClientOrder clientOrder) {
        this.clientOrder = clientOrder;
        return this;
    }

    public void setClientOrder(ClientOrder clientOrder) {
        this.clientOrder = clientOrder;
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
        Execution execution = (Execution) o;
        if (execution.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), execution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Execution{" +
            "id=" + getId() +
            ", executionId='" + getExecutionId() + "'" +
            ", executionTime='" + getExecutionTime() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
