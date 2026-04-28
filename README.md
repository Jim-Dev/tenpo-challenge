# Tenpo Challenge API - Architecture Documentation

## Sequence Diagram: Calculate Endpoint
```mermaid
sequenceDiagram
    participant Client
    participant API as CalculateController
    participant CalcSvc as CalculateService
    participant PctSvc as PercentageService
    participant Cache as Caffeine Cache (30min TTL)
    participant ExtSvc as External Mock Service

    Note over Client,API: POST /api/calculate<br/>Body: {"num1": 5, "num2": 5}

    Client->>API: POST /api/calculate
    activate API
    API->>CalcSvc: calculate(num1, num2)
    activate CalcSvc
    CalcSvc->>PctSvc: getPercentage()
    activate PctSvc
    PctSvc->>Cache: Check valid cached percentage
    alt Cache hit (valid ≤30min)
        Cache-->>PctSvc: Return cached percentage
    else Cache miss/expired
        PctSvc->>ExtSvc: Fetch percentage (Retry: max 3 attempts, exponential backoff)
        alt External service success
            ExtSvc-->>PctSvc: Return percentage value
            PctSvc->>Cache: Update cache with new percentage + timestamp
        else External service fails (after 3 retries)
            PctSvc->>Cache: Fallback to cached value (if exists)
            alt No cache available
                PctSvc-->>CalcSvc: Throw ExternalServiceException
            end
        end
    end
    PctSvc-->>CalcSvc: Return percentage
    deactivate PctSvc
    Note over CalcSvc: result = (num1 + num2) * (1 + percentage / 100)
    CalcSvc-->>API: CalculateResponse
    deactivate CalcSvc
    API-->>Client: HTTP 200: CalculateResponse
    deactivate API

    Note over Client,API: Error scenarios handled by GlobalExceptionHandler<br/>(400 validation, 404 not found, 500 server error, etc.)
```

---

## Component Architecture
| Component | Package | Description |
|-----------|---------|-------------|
| ChallengeApplication | com.tenpo.challenge | Main Spring Boot entry point, enables caching/retry via `@EnableCaching` + `@EnableRetry` |
| CalculateController | controller | REST endpoint for `POST /api/calculate`, request validation |
| CalculateService | service | Business logic for calculation: `(num1 + num2) * (1 + percentage / 100)` |
| PercentageService | service | External mock service integration, retry logic, cache fallback |
| CacheConfig | config | Caffeine cache configuration with 30-minute TTL |
| OpenApiConfig | config | OpenAPI/Swagger documentation setup, API metadata |
| GlobalExceptionHandler | controller | `@RestControllerAdvice` for HTTP error handling (4XX, 5XX) |
| ExternalServiceException | service.exceptions | Custom exception for external service failures |

---

## Data Flow Summary
| Step | Component | Input | Output |
|------|-----------|-------|--------|
| 1 | Client | POST /api/calculate | `CalculateRequest` (num1, num2) |
| 2 | CalculateController | `CalculateRequest` | Validates input, calls `CalculateService` |
| 3 | CalculateService | num1, num2 | Calls `PercentageService.getPercentage()` |
| 4 | PercentageService | - | Checks cache → fetches external mock (with retry) → falls back to cache |
| 5 | Caffeine Cache | - | Returns cached percentage (if valid ≤30min) |
| 6 | CalculateService | num1, num2, percentage | Computes result, returns `CalculateResponse` |
| 7 | Client | HTTP 200 | `CalculateResponse` (num1, num2, percentage, result) |

---

## OpenAPI Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`
- Disabled in production via `application-prod.yml` profile