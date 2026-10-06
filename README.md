# BanQ account management API

REST API for creating and listing BanQ bank accounts.
Account data is stored in an in-memory H2 database and is lost when the application stops.

Requires Java 21.

## Run the application

```bash
./gradlew bootRun
```

An account number must be `NL25BANQ` followed by 10 digits. \
The last digit is a check digit: the sum of the other nine digits modulo 10. An invalid number returns HTTP 400.

## Run the tests

```bash
./gradlew test
./gradlew test --tests org.example.banq.web.AccountControllerTest
./gradlew test --tests org.example.banq.service.AccountServiceTest
```

## Api
The API listens on http://localhost:8080

The OpenAPI spec is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml


## Curl examples:
```bash
# create account
curl -X POST http://localhost:8080/api/v1/accounts \
  -H 'Content-Type: application/json' \
  -d '{"name":"Test User","accountNumber":"NL25BANQ0123456786"}'
  
# list accounts  
curl http://localhost:8080/api/v1/accounts  
```


