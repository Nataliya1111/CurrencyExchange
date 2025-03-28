# Currency Exchange Project

## Overview
The **Currency Exchange** project is a REST API that allows users to manage currencies, exchange rates, and perform currency conversions. The application supports viewing and editing currency lists, exchange rates, and calculating conversion amounts between different currencies.

## Technologies Used
- Jakarta Servlet
- SQLite database with JDBC
- MVC Pattern
- REST API with JSON
- Hikari Connection Pool
- Jackson
- Apache Maven
- Apache Tomcat
- Postman

## REST API Endpoints
### **Currencies**
#### **GET /currencies**
Retrieve a list of available currencies.
##### Example Response:
```json
[
    {
        "id": 1,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    {
        "id": 2,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

#### **GET /currency/EUR**
Retrieve details of a specific currency by code.
##### Example Response:
```json
{
    "id": 2,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

#### **POST /currencies**
Add a new currency to the database. Data is sent in `x-www-form-urlencoded` format.
##### Example Form Fields:
- name = "Euro"
- code = "EUR"
- sign = "€"
##### Example Response:
```json
{
    "id": 2,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

### **Exchange Rates**
#### **GET /exchangeRates**
Retrieve a list of all exchange rates.
##### Example Response:
```json
[
    {
        "id": 1,
        "baseCurrency": {
            "id": 1,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 2,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
```

#### **GET /exchangeRate/USDRUB**
Retrieve a specific exchange rate by currency codes.
##### Example Response:
```json
{
    "id": 1,
    "baseCurrency": {
        "id": 1,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 3,
        "name": "Russian Ruble",
        "code": "RUB",
        "sign": "₽"
    },
    "rate": 80
}
```

#### **POST /exchangeRates**
Add a new exchange rate. Data is sent in `x-www-form-urlencoded` format.
##### Example Form Fields:
- baseCurrencyCode = "USD"
- targetCurrencyCode = "EUR"
- rate = 0.99
##### Example Response:
```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

#### **PATCH /exchangeRate/USDRUB**
Update an existing exchange rate. Data is sent in `x-www-form-urlencoded` format.
##### Example Form Field:
- rate = 80
##### Example Response:
```json
{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 2,
        "name": "Russian Ruble",
        "code": "RUB",
        "sign": "₽"
    },
    "rate": 80
}
```

### **Currency Exchange Calculation**
#### **GET /exchange?from={base}&to={target}&amount={amount}**
Convert an amount from one currency to another.
##### Example Request:
`GET /exchange?from=USD&to=AUD&amount=10`
##### Example Response:
```json
{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A$"
    },
    "rate": 1.45,
    "amount": 10.00,
    "convertedAmount": 14.50
}
```

## Deployment Instructions
1. Compile and build the WAR file using IntelliJ IDEA.
2. Deploy the WAR file to the `webapps` directory of Tomcat.
3. Restart Tomcat to apply changes.
