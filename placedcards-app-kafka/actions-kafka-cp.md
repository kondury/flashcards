Получить список топиков
```shell
docker exec -ti kafka-cp /usr/bin/kafka-topics --list --bootstrap-server localhost:9091
```

Отправить сообщение
```shell
docker exec -ti kafka-cp /usr/bin/kafka-console-producer --topic flashcards-placedcards-in-v1 --bootstrap-server localhost:9091
```

Получить сообщения
```shell
docker exec -ti kafka-cp /usr/bin/kafka-console-consumer --from-beginning --topic flashcards-placedcards-out-v1 --bootstrap-server localhost:9091 
```

Получить сообщения как consumer1
```shell
docker exec -ti kafka-okkafkaex /usr/bin/kafka-console-consumer --group consumer1 --topic flashcards-placedcards-out-v1 --bootstrap-server localhost:9091 
```

Отправить сообщение c ключом через двоеточие (key:value)
```shell
docker exec -ti kafka-cp /usr/bin/kafka-console-producer --topic topic1 --property "parse.key=true" --property "key.separator=:" --bootstrap-server localhost:9091
```
