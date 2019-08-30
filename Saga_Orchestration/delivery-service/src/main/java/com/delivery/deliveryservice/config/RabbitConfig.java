package com.delivery.deliveryservice.config;

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

import com.delivery.deliveryservice.listener.DeliveryEventListener;

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
	public DirectExchange deliveryRequestExchange() {
		return new DirectExchange(env.getProperty("delivery-service.request.exchange"), true, false);
	}
	/* ##################End Request Exchanges################## */
	
	/* ##################Start Request Queues################## */
	@Bean
	public Queue deliveryRequestQueue() {
		return new Queue(env.getProperty("delivery-service.request.queue"), true, false, false);
	}
	/* ##################End Request Queues################## */
	
	/* ##################Start Request Bindings################## */
	@Bean
    Binding deliveryRequestBinding() {
        return BindingBuilder.bind(deliveryRequestQueue()).to(deliveryRequestExchange()).with(env.getProperty("delivery-service.request.routing.key"));
    }
	/* ##################End Request Bindings################## */
	
	/* ##################Start Reply Exchanges################## */
	@Bean
	public DirectExchange deliveryReplyExchange() {
		return new DirectExchange(env.getProperty("delivery-service.reply.exchange"), true, false);
	}
	/* ##################End Reply Exchanges################## */
	
	/* ##################Start Reply Queues################## */
	@Bean
	public Queue deliveryReplyQueue() {
		return new Queue(env.getProperty("delivery-service.reply.queue"), true, false, false);
	}
	/* ##################End Reply Queues################## */
	
	/* ##################Start Reply Bindings################## */
	@Bean
    Binding deliveryReplyBinding() {
        return BindingBuilder.bind(deliveryReplyQueue()).to(deliveryReplyExchange()).with(env.getProperty("delivery-service.reply.routing.key"));
    }
	/* ##################End Reply Bindings################## */
	
	/* ##################Start Listener Containers################## */
	@Bean("deliveryListenerContainer")
    SimpleMessageListenerContainer deliveryListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conFac);
        container.setQueueNames(env.getProperty("delivery-service.request.queue"));
		container.setMessageListener(new DeliveryEventListener()); 
        return container;
    }
	/* ##################End Listener Containers################## */
	
}
