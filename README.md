# Sequence diagram for calculate api request
```mermaid
sequenceDiagram
    participant Client
    participant API as CalculateController
    participant Calc as PercentageService

    Note over Client,API: POST /api/calculate<br/>Body: {"num1": 5, "num2": 5}

    Client->>API: POST /api/calculate
    activate API
    API->>API: calculate(num1, num2)
    API->>Calc: getPercentage()
    Calc-->>API: 10.0
    deactivate API

    Note over API: result = (5 + 5) * (1 + 10/100)<br/>result = 10 * 1.10 = 11.0

    API-->>Client: HTTP 200: <br>CalculateResponse(num1=5, num2=5, percentage=10, result=11)


```

---

## Data Flow Summary

| Step | Component | Input | Output |
|------|-----------|-------|--------|
| 1 | Client | POST /api/calculate | CalculateRequest |
| 2 | CalculateController | CalculateRequest | Calls getPercentage() |
| 3 | PercentageService | - | 10.0 (mock) |
| 4 | CalculateController | num1, num2, percentage | CalculateResponse |
| 5 | Client | HTTP 200 | CalculateResponse |