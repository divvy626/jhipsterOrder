package com.cs.microservices.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ClientOrder.
 */
@Entity
@Table(name = "client_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "order_time", nullable = false)
    private Instant orderTime;

    @ManyToOne(optional = false)
    @NotNull
    private Stock stock;

    @OneToMany(mappedBy = "clientOrder")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Execution> executions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public ClientOrder orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public ClientOrder price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ClientOrder quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getOrderTime() {
        return orderTime;
    }

    public ClientOrder orderTime(Instant orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public void setOrderTime(Instant orderTime) {
        this.orderTime = orderTime;
    }

    public Stock getStock() {
        return stock;
    }

    public ClientOrder stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Set<Execution> getExecutions() {
        return executions;
    }

    public ClientOrder executions(Set<Execution> executions) {
        this.executions = executions;
        return this;
    }

    public ClientOrder addExecution(Execution execution) {
        this.executions.add(execution);
        execution.setClientOrder(this);
        return this;
    }

    public ClientOrder removeExecution(Execution execution) {
        this.executions.remove(execution);
        execution.setClientOrder(null);
        return this;
    }

    public void setExecutions(Set<Execution> executions) {
        this.executions = executions;
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
        ClientOrder clientOrder = (ClientOrder) o;
        if (clientOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientOrder{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", orderTime='" + getOrderTime() + "'" +
            "}";
    }
}
