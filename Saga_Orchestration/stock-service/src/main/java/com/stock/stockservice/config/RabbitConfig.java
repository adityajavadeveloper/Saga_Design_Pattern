package com.stock.stockservice.config;

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
	/* ##################End Request Exchanges################## */
	
	/* ##################Start Request Queues################## */
	@Bean
	public Queue stockRequestQueue() {
		return new Queue(env.getProperty("stock-service.request.queue"), true, false, false);
	}
	/* ##################End Request Queues################## */
	
	/* ##################Start Request Bindings################## */
	@Bean
    Binding stockRequestBinding() {
        return BindingBuilder.bind(stockRequestQueue()).to(stockRequestExchange()).with(env.getProperty("stock-service.request.routing.key"));
    }
	/* ##################End Request Bindings################## */
	
	/* ##################Start Reply Exchanges################## */
	@Bean
	public DirectExchange stockReplyExchange() {
		return new DirectExchange(env.getProperty("stock-service.reply.exchange"), true, false);
	}
	/* ##################End Reply Exchanges################## */
	
	/* ##################Start Reply Queues################## */
	@Bean
	public Queue stockReplyQueue() {
		return new Queue(env.getProperty("stock-service.reply.queue"), true, false, false);
	}
	/* ##################End Reply Queues################## */
	
	/* ##################Start Reply Bindings################## */
	@Bean
    Binding stockReplyBinding() {
        return BindingBuilder.bind(stockReplyQueue()).to(stockReplyExchange()).with(env.getProperty("stock-service.reply.routing.key"));
    }
	/* ##################End Reply Bindings################## */
	
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
