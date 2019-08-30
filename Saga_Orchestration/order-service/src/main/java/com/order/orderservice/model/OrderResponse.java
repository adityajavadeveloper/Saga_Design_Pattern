package com.order.orderservice.model;

import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {
	private long orderId;
	private long custId;
	private List<String> itemList;
	private String orderStatus;
	private long totalPrice;
	private String systemDeliveryNo;
}
