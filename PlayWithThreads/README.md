http://localhost:8080/tasks/counts
http://localhost:8080/start-tasks?taskCount=10


bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

curl -X POST "http://localhost:8080/send-message?message=Merhaba Kafka"
