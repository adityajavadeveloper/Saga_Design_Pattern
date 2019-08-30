package com.saga.orchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
	private long custId;
	private long totalPrice;
	private String respCode;
	private String respMsg;
}
