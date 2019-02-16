package com.example.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@SpringBootApplication
public class KafkaMessagingSystemApplication {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(KafkaMessagingSystemApplication.class, args);
		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);
		
		producer.sendMessage("Hello, World!");
		listener.latch.await(10, TimeUnit.SECONDS);
		context.close();
	}
	
	@Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

	public static class MessageProducer {
		
		@Autowired
		private KafkaTemplate<String, String> kafkaTemplate;
		
		@Value(value = "${message.topic.name}")
		private String topicName;
		
		public void sendMessage(String message) {
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
			
			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>(){

				@Override
				public void onSuccess(SendResult<String, String> result) {
					System.out.println("Sent message=[" + message + 
				              "] with offset=[" + result.getRecordMetadata().offset() + "]");
				}

				@Override
				public void onFailure(Throwable ex) {
					System.out.println("Unable to send message=["
				              + message + "] due to : " + ex.getMessage());
				}
				
			});
		}
	}
	
	public static class MessageListener{
		private CountDownLatch latch = new CountDownLatch(2);
		
		@KafkaListener(topics = "${message.topic.name}", groupId = "test", containerFactory = "kafkaListenerContainerFactory")
		public void listen(String message) {
		    System.out.println("Received Messasge in group test: " + message);
		}
	}
}

