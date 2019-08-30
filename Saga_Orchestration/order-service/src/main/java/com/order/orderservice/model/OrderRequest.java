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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class OrderRequest {
	@JsonIgnore(value = true)
	@ApiModelProperty(required = false, hidden = true)
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
	
	@JsonIgnore(value = true)
	@ApiModelProperty(required = false, hidden = true)
	private String orderStatus;

	@JsonIgnore(value = true)
	@ApiModelProperty(required = false, hidden = true)
	private long totalPrice;
	
	@JsonIgnore(value = true)
	@ApiModelProperty(required = false, hidden = true)
	private long systemDeliveryNo;
}
