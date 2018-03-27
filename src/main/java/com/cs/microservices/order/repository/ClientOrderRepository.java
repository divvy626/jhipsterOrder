package com.cs.microservices.order.repository;

import com.cs.microservices.order.domain.ClientOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClientOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {

}
