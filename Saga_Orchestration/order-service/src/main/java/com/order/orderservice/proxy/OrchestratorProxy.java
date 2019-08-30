package com.order.orderservice.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.order.orderservice.model.OrderRequest;
import com.order.orderservice.model.OrderResponse;

@FeignClient("saga-orchestrator")
@RibbonClient("saga-orchestrator")
public interface OrchestratorProxy {
	
	@RequestMapping(value = "orchestrate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderResponse orchestrate(@SpringQueryMap OrderRequest orderRequest);

}
