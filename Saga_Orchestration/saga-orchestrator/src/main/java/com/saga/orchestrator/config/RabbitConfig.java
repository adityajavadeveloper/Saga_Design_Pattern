package com.saga.orchestrator.config;

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
	
	@Bean
	public DirectExchange deliveryRequestExchange() {
		return new DirectExchange(env.getProperty("delivery-service.request.exchange"), true, false);
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
	
	@Bean
	public Queue deliveryRequestQueue() {
		return new Queue(env.getProperty("delivery-service.request.queue"), true, false, false);
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
	
	@Bean
    Binding deliveryRequestBinding() {
        return BindingBuilder.bind(deliveryRequestQueue()).to(deliveryRequestExchange()).with(env.getProperty("delivery-service.request.routing.key"));
    }
	/* ##################End Request Bindings################## */
	
	/* ##################Start Reply Exchanges################## */
	@Bean
	public DirectExchange stockReplyExchange() {
		return new DirectExchange(env.getProperty("stock-service.reply.exchange"), true, false);
	}
	
	@Bean
	public DirectExchange paymentReplyExchange() {
		return new DirectExchange(env.getProperty("payment-service.reply.exchange"), true, false);
	}
	
	@Bean
	public DirectExchange deliveryReplyExchange() {
		return new DirectExchange(env.getProperty("delivery-service.reply.exchange"), true, false);
	}
	/* ##################End Reply Exchanges################## */
	
	/* ##################Start Reply Queues################## */
	@Bean
	public Queue stockReplyQueue() {
		return new Queue(env.getProperty("stock-service.reply.queue"), true, false, false);
	}
	
	@Bean
	public Queue paymentReplyQueue() {
		return new Queue(env.getProperty("payment-service.reply.queue"), true, false, false);
	}
	
	@Bean
	public Queue deliveryReplyQueue() {
		return new Queue(env.getProperty("delivery-service.reply.queue"), true, false, false);
	}
	/* ##################End Reply Queues################## */
	
	/* ##################Start Reply Bindings################## */
	@Bean
    Binding stockReplyBinding() {
        return BindingBuilder.bind(stockReplyQueue()).to(stockReplyExchange()).with(env.getProperty("stock-service.reply.routing.key"));
    }
	
	@Bean
    Binding paymentReplyBinding() {
        return BindingBuilder.bind(paymentReplyQueue()).to(paymentReplyExchange()).with(env.getProperty("payment-service.reply.routing.key"));
    }
	
	@Bean
    Binding deliveryReplyBinding() {
        return BindingBuilder.bind(deliveryReplyQueue()).to(deliveryReplyExchange()).with(env.getProperty("delivery-service.reply.routing.key"));
    }
	/* ##################End Reply Bindings################## */
	
	/* ##################Start Rabbit Templates################## */
	@Bean("stockRabbitTemplate")
    public RabbitTemplate stockRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setExchange(env.getProperty("stock-service.request.exchange"));
        template.setRoutingKey(env.getProperty("stock-service.request.routing.key"));
        template.setReplyAddress(env.getProperty("stock-service.reply.exchange") + "/" + env.getProperty("stock-service.reply.routing.key"));
        return template;
    }
	
	@Bean("paymentRabbitTemplate")
    public RabbitTemplate paymentRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setExchange(env.getProperty("payment-service.request.exchange"));
        template.setRoutingKey(env.getProperty("payment-service.request.routing.key"));
        template.setReplyAddress(env.getProperty("payment-service.reply.exchange") + "/" + env.getProperty("payment-service.reply.routing.key"));
        return template;
    }
	
	@Bean("deliveryRabbitTemplate")
    public RabbitTemplate deliveryRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        template.setExchange(env.getProperty("delivery-service.request.exchange"));
        template.setRoutingKey(env.getProperty("delivery-service.request.routing.key"));
        template.setReplyAddress(env.getProperty("delivery-service.reply.exchange") + "/" + env.getProperty("delivery-service.reply.routing.key"));
        return template;
    }
	/* ##################End Rabbit Templates################## */
	
	/* ##################Start Listener Containers################## */
	@Bean("stockListenerContainer")
    SimpleMessageListenerContainer stockListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conFac);
        container.setQueueNames(env.getProperty("stock-service.reply.queue"));
		container.setMessageListener(stockRabbitTemplate(conFac)); 
        return container;
    }
	
	@Bean("paymentListenerContainer")
    SimpleMessageListenerContainer paymentListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conFac);
        container.setQueueNames(env.getProperty("payment-service.reply.queue"));
		container.setMessageListener(paymentRabbitTemplate(conFac)); 
        return container;
    }
	
	@Bean("deliveryListenerContainer")
    SimpleMessageListenerContainer deliveryListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(conFac);
        container.setQueueNames(env.getProperty("delivery-service.reply.queue"));
		container.setMessageListener(deliveryRabbitTemplate(conFac)); 
        return container;
    }
	/* ##################End Listener Containers################## */
	
}
