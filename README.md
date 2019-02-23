# Kafka-Messaging-System
The system manages events and send messages through kafka to the target systems.

To start install kafka on your server.

Start zookeeper using the following command:
  - zookeeper-server-start.sh config/zookeeper.properties.
The command starts zookeeper at port 2181 by default.

Then start kafka server using the following command:
  - kafka-server-start.sh config/server.properties.
The command starts kafka server at port 9092 by default.

The topic can be created from the console using the following command:
  - kafka-topics.sh --zookeeper localhost:2181 --create --topic {topic-name}
                        OR
The topic can be created by calling the Kafka Topic Configuration class present in
the source code and creating a new topic object.
                      
The topic at which events are published is called "original_event".

