расположение CLI скриптов: /opt/bitnami/kafka/bin/

Образец корректного запроса для отправки из командной строки:
{"requestType":"createCard","requestId":"create-req","debug":{"mode":"stub","stub":"success"},"card":{"front":"Fronttext","back":"Backtext"}}

Список топиков
```shell
docker exec -it deploy-kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server kafka:9092
```

Отправка сообщений во входящий топик
```shell
docker exec -it deploy-kafka-1 /opt/bitnami/kafka/bin/kafka-console-producer.sh --broker-list kafka:9092 --topic flashcards-cards-in-v1
```

Просмотр входящих сообщений
```shell
docker exec -it deploy-kafka-1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --group console-in --topic flashcards-cards-in-v1 --from-beginning
```

Просмотр исходящих сообщений
```shell
docker exec -it deploy-kafka-1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --group console-out --topic flashcards-cards-out-v1 --from-beginning
```

Создание нового топика:
```shell
docker exec -it deploy-kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1 --topic topic-name
```

Параметры топика
```shell
docker exec -it deploy-kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --describe --bootstrap-server kafka:9092 --topic topic-name
```
