# 🧠 Churn Insight API (Java)

API em **Java / Spring Boot** desenvolvida para o **Hackathon ONE**, responsável por expor o endpoint de predição de churn e **orquestrar a chamada** para um **microserviço de Machine Learning (Python)**.

✅ **Status atual:**  
A predição **já é realizada pelo ML Service Python**, consumida via `MlPredictionClient`.  
A API Java atua exclusivamente como **camada de contrato, validação e orquestração**, mantendo **baixo acoplamento** com a camada de Machine Learning.

> Esta API **não gera datasets**, **não treina modelos** e **não contém scripts de Data Science**.

---

## 🚀 Tecnologias Utilizadas

- Java 17  
- Spring Boot 3  
- Maven  
- H2 Database (in-memory – MVP)  
- MapStruct  
- Lombok  
- Springdoc OpenAPI (Swagger)  

---

## 🧩 Arquitetura

A API Java foi projetada seguindo princípios de **separação de responsabilidades**:

- Contrato REST público
- Validação dos dados de entrada
- Orquestração da chamada ao ML Service
- Padronização da resposta

Integração entre serviços via **HTTP + JSON**, permitindo deploy e evolução independentes:

```

Client → API Java (/api/predict) → ML Service Python (/predict)

```

---

## 📌 Estrutura do Projeto

```

src/main/java/com/churninsight/api
├── client            → MlPredictionClient
├── config            → WebClientConfig
├── controller        → PredictionController
├── dto               → CustomerInputDto, PredictionResponseDto
├── mapper            → PredictionMapper
├── model             → PredictionModel
│   └── enums         → Gender, ContractType, PaymentMethod, etc.
├── service           → PredictionService
└── util              → PredictionUtils (legado / referência)

````

> A camada `util` contém apenas lógica **histórica/de referência**.  
> O cálculo efetivo de churn é realizado pelo **ML Service Python**.

---

## 🔮 Endpoint Principal

### **POST /api/predict**

Recebe os dados de um cliente e retorna a previsão de churn.

A API Java repassa a requisição ao ML Service Python e retorna a resposta padronizada ao cliente.

---

## 📥 Contrato de Requisição — Exemplo (Alto risco de churn)

```json
{
  "gender": "FEMALE",
  "seniorCitizen": true,
  "partner": true,
  "dependents": true,
  "contractMonths": 2,
  "phoneService": true,
  "multipleLines": "NO",
  "internetService": "DSL",
  "onlineSecurity": "NO",
  "onlineBackup": "NO",
  "deviceProtection": "NO",
  "techSupport": "NO",
  "streamingTV": "NO",
  "streamingMovies": "NO",
  "contractType": "MONTH_TO_MONTH",
  "paperlessBilling": true,
  "paymentMethod": "ELECTRONIC_CHECK",
  "monthlyCharges": 49.99,
  "totalCharges": 89.99
}
````

### 📤 Contrato de Resposta

```json
{
  "id": 1,
  "prediction": "Churn",
  "probability": 0.527
}
```

---

## 🔗 Integração com o ML Service

A comunicação com o serviço de Machine Learning ocorre através de `MlPredictionClient`, configurado via `WebClient`.

Configuração recomendada por ambiente:

* **Desenvolvimento local**
  `ML_SERVICE_URL=http://localhost:8000`

* **Docker / Compose**
  `ML_SERVICE_URL=http://ml-service:8000`

> A URL do ML Service deve ser configurada em `application.properties` ou `application.yml`.

---

## 🔧 Como Executar (Desenvolvimento)

### 1️⃣ Suba o ML Service Python

```bash
cd ../ml-service-python
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### 2️⃣ Suba a API Java

```bash
mvn spring-boot:run
```

API disponível em:

```
http://localhost:8080
```

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Teste rápido com curl

```bash
curl -X POST "http://localhost:8080/api/predict" \
  -H "Content-Type: application/json" \
  -d '{
    "gender": "FEMALE",
    "seniorCitizen": true,
    "partner": true,
    "dependents": true,
    "contractMonths": 2,
    "phoneService": true,
    "multipleLines": "NO",
    "internetService": "DSL",
    "onlineSecurity": "NO",
    "onlineBackup": "NO",
    "deviceProtection": "NO",
    "techSupport": "NO",
    "streamingTV": "NO",
    "streamingMovies": "NO",
    "contractType": "MONTH_TO_MONTH",
    "paperlessBilling": true,
    "paymentMethod": "ELECTRONIC_CHECK",
    "monthlyCharges": 49.99,
    "totalCharges": 89.99
  }'
```

---

## 🧠 Papel da API na Arquitetura Geral

* Interface pública do sistema
* Validação e padronização
* Orquestração de chamadas
* Integração com Machine Learning
* Evolução segura sem acoplamento com dados ou modelos

---


