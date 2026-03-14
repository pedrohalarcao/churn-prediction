# 🤖 Churn ML Service (Python)

Microserviço de **Machine Learning** responsável por executar a **predição de churn** a partir de um modelo treinado e expor essa predição via **API REST** usando **FastAPI**.

Este serviço é **consumido pela API Java (Spring Boot)** por meio de chamadas HTTP, garantindo **baixo acoplamento** e evolução independente do modelo.

---

## 🚀 Tecnologias Utilizadas

- Python 3.11
- FastAPI
- Uvicorn
- Scikit-learn
- Pandas / NumPy
- Modelo serializado (`.pkl`)

---

## 🧩 Papel do ML Service na Arquitetura

Responsabilidades deste serviço:

- Carregar modelo treinado (`.pkl`)
- Aplicar pré-processamento e encoding
- Executar inferência (predição)
- Expor endpoint REST para consumo externo
- Evoluir o modelo sem impactar a API Java

> A API Java **não executa lógica de ML**.
> Toda a inteligência de predição reside neste serviço.

---

## 📌 Estrutura do Projeto

```

ml-service-python/
├── app/
│   ├── main.py           # FastAPI app e endpoints
│   ├── model_loader.py   # Carregamento do modelo .pkl
│   ├── schemas.py        # Schemas de entrada e saída
│   └── settings.py       # Configurações do serviço
├── notebooks/
│   ├── best_random_forest_model.pkl
│   └── rf_artifact.pkl
└── requirements.txt

````

- `app/` → código da API e lógica de inferência
- `notebooks/` → artefatos do modelo treinado (MVP)
- `.pkl` → modelos serializados utilizados em produção

---

## 🔮 Endpoint Principal

### **POST /predict**

Recebe os dados de um cliente e retorna a previsão de churn calculada pelo modelo.

Este endpoint define o **contrato compartilhado** com a API Java.

---

## 📥 Exemplo de Requisição — Alto risco de churn

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

### 📤 Resposta

```json
{
  "prediction": "Churn",
  "probability": 0.527
}
```

---

## 🧠 Modelo de Machine Learning

* Modelo supervisionado (Random Forest)
* Treinado previamente em ambiente de Data Science
* Serializado em `.pkl`
* Saída probabilística normalizada entre `0` e `1`
* Limiar de decisão:

  * `>= 0.5` → **Churn**
  * `< 0.5` → **No Churn**

O modelo pode ser substituído ou re-treinado **sem alterar o contrato da API**.

---

## 🔧 Como Executar (Desenvolvimento)

### 1️⃣ Instalar dependências

```bash
pip install -r requirements.txt
```

### 2️⃣ Subir o serviço

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Serviço disponível em:

```
http://localhost:8000
```

Swagger UI:

```
http://localhost:8000/docs
```

---

## 🧪 Teste rápido com curl

```bash
curl -X POST "http://localhost:8000/predict" \
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

## 🔐 Princípios Arquiteturais

* Contrato único
* Baixo acoplamento
* Deploy independente
* Evolução segura do modelo

---

