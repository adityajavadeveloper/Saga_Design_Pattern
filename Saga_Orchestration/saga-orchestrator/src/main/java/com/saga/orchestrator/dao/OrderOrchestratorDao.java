package com.saga.orchestrator.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.saga.orchestrator.model.OrderRequest;

@Repository
public interface OrderOrchestratorDao extends CrudRepository<OrderRequest, Long> {

}
