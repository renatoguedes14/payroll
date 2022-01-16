package br.renato.payroll.service;

import br.renato.payroll.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@RequiredArgsConstructor
public class RabbitMQSender {

	private final RabbitTemplate rabbitTemplate;

	@Value("${spring.rabbitmq.queue}")
	public String queue;

	public void send(@Payload final EmailDTO emailDTO) {
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.convertAndSend(queue, emailDTO);
	}
}
