# API Error Handling – Examples

This document describes the standardized error responses implemented in the **Churn Insight API**.
All examples were tested using `curl` against the `/api/predict` endpoint.

The API follows a consistent error contract and always returns a `requestId` for traceability.

---

## Common Error Response Structure

```json
{
  "type": "https://api.local/errors/<error-type>",
  "title": "Short error title",
  "status": 400,
  "detail": "Human-readable description",
  "instance": "/api/predict",
  "requestId": "unique-request-id",
  "timestamp": "2025-12-24T21:12:27.434-03:00",
  "errors": [
    {
      "field": "fieldName",
      "message": "Validation message"
    }
  ]
}
```

* `errors` is optional and appears only when field-level validation fails.
* `requestId` is generated per request and also returned in the `X-Request-ID` header.

---

## 1. Successful Prediction (200 OK)

### Request

```bash
curl -i -X POST http://localhost:8080/api/predict \
  -H "Content-Type: application/json" \
  -d '{
    "gender":"FEMALE",
    "seniorCitizen":true,
    "partner":true,
    "dependents":true,
    "contractMonths":72,
    "phoneService":true,
    "multipleLines":"NO",
    "internetService":"DSL",
    "onlineSecurity":"NO",
    "onlineBackup":"NO",
    "deviceProtection":"NO",
    "techSupport":"NO",
    "streamingTV":"NO",
    "streamingMovies":"NO",
    "contractType":"MONTH_TO_MONTH",
    "paperlessBilling":true,
    "paymentMethod":"ELECTRONIC_CHECK",
    "monthlyCharges":89.99,
    "totalCharges":1000.00
  }'
```

### Response

```json
{
  "id": 1,
  "prediction": "No Churn",
  "probability": 0.2947
}
```

---

## 2. Validation Error (400 – Invalid Field)

### Scenario

Field `totalCharges` has an invalid negative value.

### Response

```json
{
  "type": "https://api.local/errors/validation",
  "title": "Validation error",
  "status": 400,
  "detail": "Some fields are invalid.",
  "instance": "/api/predict",
  "requestId": "686c87c1b2cc445083b6cb004cd41389",
  "timestamp": "2025-12-24T21:32:38.315-03:00",
  "errors": [
    {
      "field": "totalCharges",
      "message": "totalCharges must be greater than or equal to 0"
    }
  ]
}
```

---

## 3. Missing Required Field (400)

### Scenario

Required field `gender` is missing from the request.

### Response

```json
{
  "type": "https://api.local/errors/validation",
  "title": "Validation error",
  "status": 400,
  "detail": "Some fields are invalid.",
  "instance": "/api/predict",
  "requestId": "f2a1e4c9e5b441c3a2c3f9a8b0d92c11",
  "timestamp": "2025-12-24T21:40:11.221-03:00",
  "errors": [
    {
      "field": "gender",
      "message": "gender is required"
    }
  ]
}
```

---

## 4. Invalid Enum Value / Malformed JSON (400)

### Scenario

Invalid enum value or malformed JSON payload.

### Response

```json
{
  "type": "https://api.local/errors/bad-json",
  "title": "Malformed JSON",
  "status": 400,
  "detail": "Request body is invalid JSON or contains an invalid enum value.",
  "instance": "/api/predict",
  "requestId": "7377058a59bf4f8a82f8fd089f886875",
  "timestamp": "2025-12-24T21:33:34.365-03:00",
  "errors": []
}
```

---

## 5. Internal Server Error (500)

### Scenario

Unexpected server-side exception.

### Response

```json
{
  "type": "https://api.local/errors/internal",
  "title": "Internal error",
  "status": 500,
  "detail": "Unexpected error.",
  "instance": "/api/predict",
  "requestId": "a168cf5a1e4545df9f08e0a3c55caeff",
  "timestamp": "2025-12-24T21:34:36.907-03:00",
  "errors": []
}
```

---

## Notes

* All error responses follow the same contract.
* Stack traces are never exposed to the client.
* Errors are logged internally using the `requestId` for correlation.
* This structure simplifies frontend integration and debugging.

---


