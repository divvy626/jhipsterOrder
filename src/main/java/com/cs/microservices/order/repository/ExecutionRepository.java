package com.cs.microservices.order.repository;

import com.cs.microservices.order.domain.Execution;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Execution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

}
