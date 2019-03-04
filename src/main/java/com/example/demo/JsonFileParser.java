package com.example.demo;

import java.io.File;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;

import com.example.demo.KafkaMessagingSystemApplication.MessageListener;

public class JsonFileParser {

	public JSONObject jsonParser(){
		JSONParser parser = new JSONParser();
	    ClassLoader classLoader = new MessageListener().getClass().getClassLoader();
	    String fileName = "com/example/demo/EventJsonFile.json";
	    File file = new File(classLoader.getResource(fileName).getFile());
	    
	    try {
			FileReader reader = new FileReader(file.getAbsoluteFile());
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject)obj;
			return jsonObject;
//			System.out.println("The name of event is: " + jsonObject.get("name"));
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
}
