package com.example.demo;

import java.io.File;
import java.io.FileReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.NewTopic;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import org.apache.tomcat.util.json.JSONParser;
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
		
		producer.sendEventMessage(new Events("UCL", "FIFA", "12:30:00", "Biggest European Event in the field of Football"));
		listener.eventLatch.await(10, TimeUnit.SECONDS);
		
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
    
    @Bean
    public JsonFileParser jsonFileParser() {
    	return new JsonFileParser();
    }

	public static class MessageProducer {
		
		@Autowired
		private KafkaTemplate<String, String> kafkaTemplate;
		
		@Autowired
        private KafkaTemplate<String, Events> eventKafkaTemplate;
		
		@Value(value = "${message.topic.name}")		
		private String topicName;
		
		@Value(value = "${event.topic.name}")
		private String eventTopicName;
		
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

		public void sendEventMessage(Events event) {
			eventKafkaTemplate.send(eventTopicName, event);
		}
	}
	
	public static class MessageListener{
		private CountDownLatch latch = new CountDownLatch(2);
		private CountDownLatch eventLatch = new CountDownLatch(1);		
		JsonFileParser jsonFileParser = new JsonFileParser();
		
		@KafkaListener(topics = "${message.topic.name}", groupId = "test", containerFactory = "kafkaListenerContainerFactory")
		public void listen(String message) {
		    System.out.println("Received Messasge in group test: " + message);
		}
		
		@KafkaListener(topics = "${event.topic.name}", containerFactory = "eventKafkaListenerContainerFactory")
        public void eventListener(Events event) {
            System.out.println("Recieved event message: " + event);
            this.eventLatch.countDown();
            JSONObject jsonObject = jsonFileParser.jsonParser();
            System.out.println("The name of the event is: " + jsonObject.get("name"));
            System.out.println("The source of the event is: " + jsonObject.get("source"));
		}
	}
}





