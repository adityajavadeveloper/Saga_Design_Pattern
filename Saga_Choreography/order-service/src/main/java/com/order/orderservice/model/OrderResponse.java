package com.order.orderservice.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class OrderResponse {
	
	@Id
    @GeneratedValue
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
	
	@JsonIgnore(value = true)
	@Column(name="action")
	private String action;
	
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
