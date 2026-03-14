# 📊 Churn Insight — API & ML Integration

Este projeto demonstra a **integração entre um serviço de Machine Learning e uma API backend**, com foco em **arquitetura, comunicação entre serviços e separação de responsabilidades**.

O objetivo é apresentar, de forma didática, como um modelo de predição pode ser disponibilizado como serviço e consumido por uma API Java em um cenário próximo ao mundo real.

---

## 🧠 Conceito do Projeto

O projeto é dividido em **dois serviços independentes**:

- **ML Service (Python / FastAPI)**
  Responsável por carregar o pipeline/modelo treinado e executar a predição.

- **API Backend (Java / Spring Boot)**
  Responsável por receber requisições externas, encaminhar os dados ao serviço de ML e
  retornar a resposta ao cliente de forma padronizada.

Essa separação permite:
- desacoplamento entre backend e machine learning
- manutenção e evolução independentes
- integração via HTTP, como em ambientes produtivos

---

## 🏗️ Arquitetura Geral

```

Client
↓
Spring Boot API (Java)
↓
ML Service (Python / FastAPI)
↓
Prediction Result

````

- A API Java **não executa lógica de ML**
- Toda a inferência ocorre no serviço Python
- A API atua como camada de orquestração e exposição

---

## 📁 Estrutura do Repositório

```txt
churn-insight/
├── api-java/
│   ├── src/main/java/com/churninsight/api
│   │   ├── controller/       # Endpoints REST
│   │   ├── service/          # Camada de orquestração
│   │   ├── client/           # Cliente HTTP para o ML Service
│   │   ├── dto/              # DTOs de entrada e saída
│   │   ├── mapper/           # Conversões DTO ↔ Model
│   │   ├── model/            # Modelos de domínio
│   │   │   └── enums/        # Enumerações da aplicação
│   │   ├── config/           # Configurações (ex: WebClient)
│   │   └── util/             # Utilitários
│   │
│   ├── src/main/resources
│   │   └── application.properties
│   │
│   ├── src/test/             # Testes automatizados
│   ├── pom.xml               # Dependências e build
│   └── README.md             # Documentação da API
│
└── ml-service-python/
    ├── app/
    │   ├── main.py            # Entrypoint FastAPI
    │   ├── model_loader.py    # Carregamento do modelo
    │   ├── schemas.py         # Schemas de request/response
    │   └── settings.py        # Configurações do serviço
    │
    ├── notebooks/             # Experimentos e artefatos de ML
    ├── requirements.txt       # Dependências Python
    └── README.md              # Documentação do ML Service
````

---

## 🔁 Exemplos de Requisição e Resposta

A API recebe os dados do cliente, encaminha para o **ML Service** e retorna a predição de churn.

### 📥 Exemplo 1 — Cliente com **Alta probabilidade de Churn**

**Requisição (POST)**
`/predictions`

```json
{
  "gender": "FEMALE",
  "seniorCitizen": true,
  "partner": true,
  "dependents": true,
  "contractMonths": 2,
  "phoneService": true,
  "multipleLines": "NO",
  "internetService": "FIBER_OPTIC",
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
```

**Resposta**

```json
{
  "id": 1,
  "prediction": "Churn",
  "probability": 0.6115
}
```

📌 **Interpretação**
Cliente com maior risco de cancelamento (*churn*), conforme a predição retornada.

---

### 📥 Exemplo 2 — Cliente com **Baixa probabilidade de Churn**

**Requisição (POST)**
`/predictions`

```json
{
  "gender": "MALE",
  "seniorCitizen": false,
  "partner": true,
  "dependents": false,
  "contractMonths": 36,
  "phoneService": true,
  "multipleLines": "YES",
  "internetService": "DSL",
  "onlineSecurity": "YES",
  "onlineBackup": "YES",
  "deviceProtection": "YES",
  "techSupport": "YES",
  "streamingTV": "YES",
  "streamingMovies": "YES",
  "contractType": "TWO_YEAR",
  "paperlessBilling": false,
  "paymentMethod": "CREDIT_CARD",
  "monthlyCharges": 79.99,
  "totalCharges": 2879.64
}
```

**Resposta**

```json
{
  "id": 2,
  "prediction": "No Churn",
  "probability": 0.0872
}
```

📌 **Interpretação**
Cliente com baixa probabilidade de churn, indicando maior retenção.

---

## ▶️ Execução Local (Visão Geral)

### ML Service (Python)

```bash
cd ml-service-python
pip install -r requirements.txt
uvicorn app.main:app --port 8000
```

### API Backend (Java)

```bash
cd api-java
mvn spring-boot:run
```

---

## 🎯 O que este projeto ensina

* Integração entre backend e machine learning
* Comunicação entre serviços via HTTP
* Separação clara de responsabilidades
* Organização de projetos em formato **monorepo**
* Arquitetura aplicável a cenários reais

---

## 🧪 Contexto

Projeto desenvolvido no contexto de um **Hackathon**, com foco em aprendizado,
colaboração e aplicação de boas práticas de engenharia.

---


