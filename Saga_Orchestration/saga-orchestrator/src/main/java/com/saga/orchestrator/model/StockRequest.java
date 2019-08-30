package com.saga.orchestrator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {
	private long orderId;
	private List<String> itemList;
	private String action;
}
