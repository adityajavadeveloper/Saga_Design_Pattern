package com.stock.stockservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.stock.stockservice.listener.StockEventListener;

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
	public DirectExchange stockRequestExchange() {
		return new DirectExchange(env.getProperty("stock-service.request.exchange"), true, false);
	}
	
	@Bean
	public DirectExchange paymentRequestExchange() {
		return new DirectExchange(env.getProperty("payment-service.request.exchange"), true, false);
	}
	/* ##################End Request Exchanges################## */
	
	/* ##################Start Request Queues################## */
	@Bean
	public Queue stockRequestQueue() {
		return new Queue(env.getProperty("stock-service.request.queue"), true, false, false);
	}
	
	@Bean
	public Queue paymentRequestQueue() {
		return new Queue(env.getProperty("payment-service.request.queue"), true, false, false);
	}
	/* ##################End Request Queues################## */
	
	/* ##################Start Request Bindings################## */
	@Bean
    Binding stockRequestBinding() {
        return BindingBuilder.bind(stockRequestQueue()).to(stockRequestExchange()).with(env.getProperty("stock-service.request.routing.key"));
    }
	
	@Bean
    Binding paymentRequestBinding() {
        return BindingBuilder.bind(paymentRequestQueue()).to(paymentRequestExchange()).with(env.getProperty("payment-service.request.routing.key"));
    }
	/* ##################End Request Bindings################## */
	
	/* ##################Start Rabbit Templates################## */
	@Bean("paymentRabbitTemplate")
    public RabbitTemplate paymentRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setExchange(env.getProperty("payment-service.request.exchange"));
        template.setRoutingKey(env.getProperty("payment-service.request.routing.key"));
        return template;
    }
	/* ##################End Rabbit Templates################## */
	
	/* ##################Start Listener Containers################## */
	
	/* ##################Start Listener Containers################## */
	@Bean("stockListenerContainer")
    SimpleMessageListenerContainer stockListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conFac);
        container.setQueueNames(env.getProperty("stock-service.request.queue"));
		container.setMessageListener(new StockEventListener()); 
        return container;
    }
	/* ##################End Listener Containers################## */
	
}
