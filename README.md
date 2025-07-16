### Подробный README для проектов `contractor` и `deal`


### 1. Общее описание

Микросервисная система для управления:
- **contractor**: Контрагентами (клиенты/партнёры) с иерархией, фильтрацией и пагинацией
- **deal**: Сделками с привязкой контрагентов, статусами, валютами и экспортом в Excel


### 2. Технологический стек

| Компонент          | Назначение                              | Версия       |
|---------------------|-----------------------------------------|--------------|
| Java                | Основной язык разработки                | 17+          |
| Spring Boot         | Фреймворк                               | 3.2.5        |
| PostgreSQL          | База данных                             | 16           |
| MapStruct           | Маппинг DTO ↔ Entity                    | 1.5.5        |
| Lombok              | Генерация boilerplate кода              | 1.18.30      |
| Testcontainers      | Интеграционное тестирование БД          | 1.19.7       |
| Swagger             | Документация API                        | 3.0.0        |
| Apache POI          | Генерация Excel (deal)                  | 5.2.5        |


### 3. Запуск через Docker Compose

1. Перейдите в директорию нужного сервиса

2. Запустите систему:
3. 
```bash
docker-compose up --build -d
```


### 4. API Endpoints и примеры запросов

#### Сервис Contractor

**Swagger UI**: http://localhost:8080/swagger-ui.html

**1. Создание контрагента:**

```bash
curl -X PUT "http://localhost:8080/contractor/save" \
-H "Content-Type: application/json" \
-d '{
  "id": "CTR12345",
  "parentId": "CTR00001",
  "name": "ООО Ромашка",
  "nameFull": "Общество с ограниченной ответственностью Ромашка",
  "inn": "1234567890",
  "ogrn": "1234567890123",
  "country": "RU",
  "industry": 1,
  "orgForm": 2,
  "isActive": true
}'
```

**2. Поиск контрагентов:**

```bash
curl -X POST "http://localhost:8080/contractor/search?page=0&size=10" \
-H "Content-Type: application/json" \
-d '{
  "searchTerm": "Ромашка",
  "country": "Россия"
}'
```

**3. Деактивация контрагента:**

```bash
curl -X DELETE "http://localhost:8080/contractor/delete/CTR12345"
```


#### Сервис Deal (порт 8081)

**Swagger UI**: http://localhost:8081/swagger-ui.html

**1. Создание сделки:**
```bash
curl -X PUT "http://localhost:8081/deal/save" \
-H "Content-Type: application/json" \
-d '{
  "description": "Сделка №2025-07",
  "agreementNumber": "DL-2025-001",
  "agreementDate": "2025-07-17",
  "type": {"id": "LOAN", "name": "Кредит"},
  "status": {"id": "DRAFT", "name": "Черновик"},
  "sum": {
    "value": 150000.00,
    "currency": {"id": "RUB", "name": "Рубль"}
  },
  "contractors": [
    {
      "contractorId": "CTR12345",
      "name": "ООО Ромашка",
      "inn": "1234567890",
      "main": true,
      "roles": [{"id": "BORROWER", "name": "Заемщик"}]
    }
  ]
}'
```

**2. Смена статуса сделки:**

```bash
curl -X PATCH "http://localhost:8081/deal/change/status?id=3fa85f64-5717-4562-b3fc-2c963f66afa6&status=APPROVED"
```

**3. Экспорт сделок в Excel:**

```bash
curl -X POST "http://localhost:8081/deal/search/export" \
-H "Content-Type: application/json" \
-d '{"statuses":["APPROVED"]}' \
-o deals.xlsx
```

**4. Добавление контрагента к сделке:**

```bash
curl -X POST "http://localhost:8081/deal-contractor/contractor-to-role/add?contractorId=3fa85f64-5717-4562-b3fc-2c963f66afa6&roleId=GUARANTOR"
```


### 5. Тестирование

**Contractor:**

```bash
cd contractor
mvn test
```
Тесты включают:
- Интеграционные тесты с Testcontainers
- Тесты репозитория (`ContractorRepositoryTest`)
- Тесты контроллера (`ContractorControllerTest`)

**Deal:**

```bash
cd deal
mvn test
```
Тесты включают:
- Юнит-тесты сервисов
- Тесты мапперов
- Интеграционные тесты БД


### 6. Особенности реализации

**Contractor:**
- Иерархическая структура контрагентов (`parentId`)
- Фильтрация по 10+ параметрам (ИНН, ОГРН, страна и т.д.)
- Кастомная сериализация дат (Jackson)
- Логическое удаление (`is_active`)
- Поддержка 100K+ записей

**Deal:**
- Экспорт в Excel с Apache POI
- Комплексные запросы с JPA Specifications
- Статусная модель сделок (Draft → Approved → Closed)
- Привязка ролей к контрагентам (Borrower, Guarantor)
- Атомарные операции с транзакциями


### 7. Мониторинг

**Actuator Endpoints:**
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Prometheus: http://localhost:8080/actuator/prometheus
