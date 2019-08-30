package com.delivery.deliveryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
	private long orderId;
	private String systemDeliveryNo;
	private String respCode;
	private String respMsg;
	
}
