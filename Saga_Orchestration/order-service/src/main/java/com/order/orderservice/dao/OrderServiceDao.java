package com.order.orderservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.order.orderservice.model.OrderRequest;

@Repository
public interface OrderServiceDao extends CrudRepository<OrderRequest, Long> {

}
