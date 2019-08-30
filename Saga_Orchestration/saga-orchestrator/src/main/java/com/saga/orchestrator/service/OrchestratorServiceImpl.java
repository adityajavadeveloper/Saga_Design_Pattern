package com.saga.orchestrator.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.saga.orchestrator.constants.Constants;
import com.saga.orchestrator.dao.OrderOrchestratorDao;
import com.saga.orchestrator.log.LogConst;
import com.saga.orchestrator.log.LoggerSingleton;
import com.saga.orchestrator.model.DeliveryResponse;
import com.saga.orchestrator.model.OrderRequest;
import com.saga.orchestrator.model.OrderResponse;
import com.saga.orchestrator.model.PaymentResponse;
import com.saga.orchestrator.model.StockResponse;
import com.saga.orchestrator.utils.Utils;

@Service
public class OrchestratorServiceImpl implements OrchestratorService {

	@Autowired
	@Qualifier("stockRabbitTemplate")
	private RabbitTemplate stockRabbitTemplate;
	
	@Autowired
	@Qualifier("paymentRabbitTemplate")
	private RabbitTemplate paymentRabbitTemplate;
	
	@Autowired
	@Qualifier("deliveryRabbitTemplate")
	private RabbitTemplate deliveryRabbitTemplate;
	
	@Autowired
	private OrderOrchestratorDao orderOrchestratorDao;
	
	private static final String CLASSNAME = "OrchestratorServiceImpl";
	
	@Override
	public OrderResponse orchestrate(OrderRequest orderRequest) throws Throwable {
		final String methodName = "orchestrate";
		
		//Initial Insert in DB
		OrderRequest savedOrderRequest = orderOrchestratorDao.save(orderRequest);
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "After saving order in DB savedOrderRequest = " + savedOrderRequest);
		
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Block stock request = " + Utils.toStoreBean(savedOrderRequest, Constants.BLOCK_STOCK));
		StockResponse stockResponse = stockRabbitTemplate.convertSendAndReceiveAsType(Utils.toStoreBean(savedOrderRequest, Constants.BLOCK_STOCK), new ParameterizedTypeReference<StockResponse>() { });
		LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Block stock response = " + stockResponse);
		
		savedOrderRequest.setStockRespCode(stockResponse.getRespCode());
		savedOrderRequest.setStockRespDesc(stockResponse.getRespMsg());
		savedOrderRequest.setOrderStatus(stockResponse.getRespCode());
		
		//Update Stock Response in DB
		orderOrchestratorDao.save(savedOrderRequest);
		
		if(Constants.SUCCESS_CODE.equals(stockResponse.getRespCode())) {
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Debit customer wallet request = " + Utils.toPaymentBean(orderRequest, Constants.DEBIT_CUSTOMER_WALLET));
			PaymentResponse paymentResponse = paymentRabbitTemplate.convertSendAndReceiveAsType(Utils.toPaymentBean(orderRequest, Constants.DEBIT_CUSTOMER_WALLET), new ParameterizedTypeReference<PaymentResponse>() { });
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Debit customer wallet response = " + paymentResponse);
			
			savedOrderRequest.setPaymentRespCode(paymentResponse.getRespCode());
			savedOrderRequest.setPaymentRespDesc(paymentResponse.getRespMsg());
			savedOrderRequest.setTotalPrice(paymentResponse.getTotalPrice());
			savedOrderRequest.setOrderStatus(paymentResponse.getRespCode());
			
			//Update Payment Response in DB
			orderOrchestratorDao.save(savedOrderRequest);
			
			if(Constants.SUCCESS_CODE.equals(paymentResponse.getRespCode())) {
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Schedule delivery request = " + Utils.toDeliveryBean(orderRequest, Constants.SCHEDULE_DELIVERY));
				DeliveryResponse deliveryResponse = deliveryRabbitTemplate.convertSendAndReceiveAsType(Utils.toDeliveryBean(orderRequest, Constants.SCHEDULE_DELIVERY), new ParameterizedTypeReference<DeliveryResponse>() { });
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Schedule delivery response = " + deliveryResponse);
				
				savedOrderRequest.setDeliveryRespCode(deliveryResponse.getRespCode());
				savedOrderRequest.setDeliveryRespDesc(deliveryResponse.getRespMsg());
				savedOrderRequest.setSystemDeliveryNo(deliveryResponse.getSystemDeliveryNo());
				savedOrderRequest.setOrderStatus(deliveryResponse.getRespCode());
				
				//Update Delivery Response in DB
				orderOrchestratorDao.save(savedOrderRequest);
			}else {
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Revert stock request = " + Utils.toStoreBean(savedOrderRequest, Constants.REVERT_STOCK));
				StockResponse revertStockResponse = stockRabbitTemplate.convertSendAndReceiveAsType(Utils.toStoreBean(savedOrderRequest, Constants.REVERT_STOCK), new ParameterizedTypeReference<StockResponse>() { });
				LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Revert stock response = " + revertStockResponse);
				
				savedOrderRequest.setStockRespCode(revertStockResponse.getRespCode());
				savedOrderRequest.setStockRespDesc(revertStockResponse.getRespMsg());
				savedOrderRequest.setOrderStatus(revertStockResponse.getRespCode());
				
				//Update Stock Response in DB
				orderOrchestratorDao.save(savedOrderRequest);
			}
			LoggerSingleton.log(LogConst.INFO, CLASSNAME, methodName, "Final order response = " + Utils.toOrderResponse(savedOrderRequest));
		}
		
		return Utils.toOrderResponse(savedOrderRequest);
	}

}
