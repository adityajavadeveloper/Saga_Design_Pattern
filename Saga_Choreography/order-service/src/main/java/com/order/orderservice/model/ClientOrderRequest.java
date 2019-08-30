package com.order.orderservice.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;

import lombok.Data;

@Data
public class ClientOrderRequest {
	
	@Column(name="cust_id")
	private long custId;
	
	@ElementCollection
    @CollectionTable(name = "order_item_list", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "item_list")
	private List<String> itemList;
	
}
