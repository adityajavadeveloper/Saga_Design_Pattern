package com.saga.orchestrator.service;

import com.saga.orchestrator.model.OrderRequest;
import com.saga.orchestrator.model.OrderResponse;

public interface OrchestratorService {
	public OrderResponse orchestrate(OrderRequest orderRequest) throws Throwable;
}
