package com.saga.orchestrator.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="orders_orchestrate")
public class OrderRequest {
	
	@Id
    @GeneratedValue
    @Column(name="orchestrator_order_id")
	private long orchestratorOrderId;
	
    @Column(name="order_id")
	private long orderId;
	
	@Column(name="cust_id")
	private long custId;
	
	@ElementCollection
    @CollectionTable(name = "order_item_list", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "item_list")
	private List<String> itemList;
	
	@Column(name="order_status")
	private String orderStatus;
	
	@Column(name="total_price")
	private long totalPrice;
	
	@Column(name="system_delivery_no")
	private String systemDeliveryNo;
	
	@Column(name="stock_resp_code")
	private String stockRespCode;
	
	@Column(name="stock_resp_desc")
	private String stockRespDesc;
	
	@Column(name="payment_resp_code")
	private String paymentRespCode;
	
	@Column(name="payment_resp_desc")
	private String paymentRespDesc;
	
	@Column(name="delivery_resp_code")
	private String deliveryRespCode;
	
	@Column(name="delivery_resp_desc")
	private String deliveryRespDesc;
}
