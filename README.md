# App for loan applications

## Tech requirements:

Spring Boot
REST interface

available operations are:
* apply for loan (term, amount)
  if application is not within amount/term range then reject application
  if application is between 00:00 and 06:00 and max amount is asked then reject application
  issued loan has 10% of principal (not 10% per year)
* extend loan
  extension term is preconfigured. Upon extension the due date is changed, original due date + term
* fetch loan
  should return amount, due date

define max/min amount and max/min term (days)
no installments
junit tests
integration success path scenario test (loan issued)
no GUI
no authorization
no users

Non-tech requirements:

* code has to be easy to extend/modify
* SOLID, KISS, DRY, etc
* post the solution to a public repo (github, bitbucket, etc)

## Run automated tests

`./mvnw clean test`

## Run application

`./mvnw spring-boot:run`

## Manual Testing

By default, server starts on port 8080. It can be changed in `application.properties` is needed.

### Apply for loan
```shell
curl --location --request POST 'localhost:8080/loans' \
--header 'Content-Type: application/json' \
--data-raw '{
"term":10,
"amount":5000
}'
```


### Get all loans
```shell
curl --location --request GET 'localhost:8080/loans'
```

### Get single loan data
```shell
curl --location --request GET 'localhost:8080/loans/1'
```

### Extend loan
```shell
curl --location --request POST 'localhost:8080/loans/1/extend'
```