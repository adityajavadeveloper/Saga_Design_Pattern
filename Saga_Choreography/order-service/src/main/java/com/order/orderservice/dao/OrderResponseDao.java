package com.order.orderservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.order.orderservice.model.OrderResponse;

@Repository
public interface OrderResponseDao extends CrudRepository<OrderResponse, Long> {

}
