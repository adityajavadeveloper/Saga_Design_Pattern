package com.payment.paymentservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.payment.paymentservice.listener.PaymentEventListener;

@Configuration
public class RabbitConfig {
	
	@Autowired
	private Environment env;

	@Autowired
	private ConnectionFactory conFac;
	
	@Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	
	/* ##################Start Request Exchanges################## */
	@Bean
	public DirectExchange paymentRequestExchange() {
		return new DirectExchange(env.getProperty("payment-service.request.exchange"), true, false);
	}
	/* ##################End Request Exchanges################## */
	
	/* ##################Start Request Queues################## */
	@Bean
	public Queue paymentRequestQueue() {
		return new Queue(env.getProperty("payment-service.request.queue"), true, false, false);
	}
	/* ##################End Request Queues################## */
	
	/* ##################Start Request Bindings################## */
	@Bean
    Binding paymentRequestBinding() {
        return BindingBuilder.bind(paymentRequestQueue()).to(paymentRequestExchange()).with(env.getProperty("payment-service.request.routing.key"));
    }
	/* ##################End Request Bindings################## */
	
	/* ##################Start Reply Exchanges################## */
	@Bean
	public DirectExchange paymentReplyExchange() {
		return new DirectExchange(env.getProperty("payment-service.reply.exchange"), true, false);
	}
	/* ##################End Reply Exchanges################## */
	
	/* ##################Start Reply Queues################## */
	@Bean
	public Queue paymentReplyQueue() {
		return new Queue(env.getProperty("payment-service.reply.queue"), true, false, false);
	}
	/* ##################End Reply Queues################## */
	
	/* ##################Start Reply Bindings################## */
	@Bean
    Binding paymentReplyBinding() {
        return BindingBuilder.bind(paymentReplyQueue()).to(paymentReplyExchange()).with(env.getProperty("payment-service.reply.routing.key"));
    }
	/* ##################End Reply Bindings################## */
	
	/* ##################Start Listener Containers################## */
	@Bean("paymentListenerContainer")
    SimpleMessageListenerContainer paymentListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conFac);
        container.setQueueNames(env.getProperty("payment-service.request.queue"));
		container.setMessageListener(new PaymentEventListener()); 
        return container;
    }
	/* ##################End Listener Containers################## */
	
}
