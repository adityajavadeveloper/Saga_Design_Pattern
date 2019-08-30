package com.saga.orchestrator.utils;

import com.saga.orchestrator.model.DeliveryRequest;
import com.saga.orchestrator.model.OrderRequest;
import com.saga.orchestrator.model.OrderResponse;
import com.saga.orchestrator.model.PaymentRequest;
import com.saga.orchestrator.model.StockRequest;

public class Utils {

	public static StockRequest toStoreBean(OrderRequest orderRequest, String action) {
		return new StockRequest(orderRequest.getOrderId(), orderRequest.getItemList(), action);
	}
	
	public static PaymentRequest toPaymentBean(OrderRequest orderRequest, String action) {
		return new PaymentRequest(orderRequest.getCustId(), orderRequest.getTotalPrice());
	}
	
	public static DeliveryRequest toDeliveryBean(OrderRequest orderRequest, String action) {
		return new DeliveryRequest(orderRequest.getOrderId(), orderRequest.getCustId(), orderRequest.getItemList());
	}
	
	public static OrderResponse toOrderResponse(OrderRequest orderRequest) {
		return new OrderResponse(orderRequest.getOrderId(), orderRequest.getCustId(), orderRequest.getItemList(), orderRequest.getOrderStatus(), orderRequest.getTotalPrice(), orderRequest.getSystemDeliveryNo());
	}
	
}
