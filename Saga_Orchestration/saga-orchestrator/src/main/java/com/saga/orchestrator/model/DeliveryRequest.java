package com.saga.orchestrator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {
	private long orderId;
	private long custId;
	private List<String> itemList;
}
