package com.payment.paymentservice.model;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
	private long orderId;
	private long custId;
	private List<String> itemList;
	private String orderStatus;
	private long totalPrice;
	private String systemDeliveryNo;
	private String action;
	private String stockRespCode;
	private String stockRespDesc;
	private String paymentRespCode;
	private String paymentRespDesc;
	private String deliveryRespCode;
	private String deliveryRespDesc;
}
