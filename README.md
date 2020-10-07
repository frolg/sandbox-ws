# Sandbox WS 
Тестовое приложение для работы с Spring Reactive WebSocket API.
Предназначено для отправки клиентам сообщений (push - нотификации).

## Модули
### server

- RESTful Web Service

Обработка запросов на изменение данных

- WebSocket server

Отправка push-нотификаций клиентам об изменении данных 

### client

Содержит две реализации WebSocket клиентов: js и spring boot application

## Запуск

### Запуск сервера

`./gradlew :modules:server:bootRun`

### Запуск клиента

Вариант 1 - spring boot application

`./gradlew :modules:server:bootRun`

Вариант 2 - js

Открыть в браузере /sandbox-ws/modules/client/src/main/resources/client1.html и/или 
/sandbox-ws/modules/client/src/main/resources/client2.html

Оба варианта можно использовать одновременно.

### Отправка запроса на обновление данных

`curl -X POST http://localhost:8080/rest/update -H 'Content-Type: application/json' -H "Authorization: 1001"  -d '{"clientId":1001, "newValue": "bbb"}'`

В заголовке Authorization можно указывать любое из значений 1001 или 1002, он используется только для аутентификации и 
не влияет на поведение системы.

Если в теле запроса указан "clientId":1001, то нотификацию получит спринговый клиент и клиент из client1.html,
а если указан "clientId":1002, то нотификацию получит клиент из client2.html