#execute under linux env in :~/kafka_2.13-3.6.1


#zookeeper
/bin/zookeeper-server-start.sh ./config/zookeeper.properties

#kafka
./bin/kafka-server-start.sh ./config/server.properties

#console-producer
./bin/kafka-console-producer.sh --topic quickstart-events  --bootstrap-server 172.20.142.208:9092

#consumer
bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server 172.20.142.208:9092
bin/kafka-console-consumer.sh --topic streams-pipe-output  --from-beginning --bootstrap-server 172.20.142.208:9092
