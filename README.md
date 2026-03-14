# 🚀 MVP de Previsão de Churn de Clientes  

Este projeto foi desenvolvido com o objetivo de construir um **MVP completo para previsão de churn (evasão de clientes)** para uma empresa de telecomunicações.

A solução contempla:

- 📊 Engenharia e preparação de dados  
- 🤖 Desenvolvimento e otimização do modelo de Machine Learning  
- 🔌 API Back-End em Java/Spring Boot  
- 🌐 Interface Web (Front-End) para interação do usuário  
- 🔄 Integração entre modelo preditivo, API e aplicação web  

---

# 🎯 Objetivo do Projeto

Construir uma solução end-to-end capaz de:

- Prever a probabilidade de evasão de clientes de telecom
- Disponibilizar a predição via API
- Permitir simulação interativa através de uma interface web
- Estruturar uma base pronta para evolução para ambiente produtivo

---

# 📊 1. Engenharia de Dados & Machine Learning

## 📥 Dataset

Fonte dos dados:  
https://www.kaggle.com/datasets/blastchar/telco-customer-churn

---

## 🔄 Pipeline de Preparação de Dados

### ✔ Carregamento
- Leitura do dataset via Pandas diretamente do GitHub.

### ✔ Tratamento de Dados
- Tradução de colunas para português
- Conversão da coluna `TotalCharges` para numérico
- Remoção de valores nulos
- Remoção da coluna `ID_Cliente`

### ✔ Feature Engineering
- Codificação de variáveis binárias (0/1)
- One-Hot Encoding para variáveis multiclasse
- Escalonamento com `StandardScaler`

### ✔ Divisão dos Dados
- 80% treino / 20% teste
- `train_test_split(random_state=42)`

---

## 🤖 Modelagem Preditiva

### Modelos Avaliados

- Dummy Classifier (baseline)
- Decision Tree
- Random Forest

### Métricas Utilizadas

- Accuracy
- Precision
- Recall
- F1-Score
- ROC AUC
- Matriz de Confusão

---

## ⚙️ Otimização

- GridSearchCV aplicado ao RandomForest
- Métrica de otimização: F1-Score
- Ajuste de:
  - n_estimators
  - max_depth
  - min_samples_split
  - min_samples_leaf

O modelo otimizado apresentou melhora geral de desempenho.

---

## 💾 Persistência do Modelo

- Modelo salvo com `pickle`
- Arquivo gerado:  
`rf-artifact.pkl`

---

# 🔌 2. Back-End – API de Predição

API desenvolvida em **Java 17 com Spring Boot 3**, responsável por calcular a probabilidade de churn com base nos dados enviados pelo cliente.

---

## 📌 Funcionalidades da API

- Endpoint principal:

  
- Estrutura de DTOs
- Modelo de predição mockado (primeira versão)
- Regras iniciais de cálculo em `PredictionUtils`
- Swagger UI para testes rápidos
- Estrutura para geração de dataset sintético

---

## 🧰 Tecnologias do Back-End

- Java 17
- Spring Boot 3
- Maven
- H2 Database (in-memory)
- MapStruct
- Lombok
- Swagger (Springdoc OpenAPI)
- Python (geração de dataset sintético)

---

## 🔄 Fluxo da API

1. Recebe dados do cliente via requisição HTTP
2. Mapeia para DTO
3. Processa cálculo da probabilidade
4. Retorna:
   - Status de evasão (Sim/Não)
   - Probabilidade (%)

---

# 🌐 3. Página Web

Interface desenvolvida para permitir que usuários interajam com o modelo de predição.

---

## 🎯 Objetivo

- Permitir simulação de churn via formulário
- Exibir resultado em tempo real
- Conectar com a API

---

## 🧩 Funcionalidades

- Formulário com campos como:
  - Tipo de contrato
  - Serviços contratados
  - Possui dependentes
  - Tempo de permanência
  - Custos mensais
  - Custos totais
- Validação básica de dados
- Envio via requisição HTTP para API
- Exibição da probabilidade de churn

---

## 🧰 Tecnologias do Front-End (Página Web)

- HTML
- CSS
- JavaScript
- Visual Studio Code

---

# 🔄 4. Integração Completa

### Fluxo da Aplicação

1. Usuário preenche formulário na página web
2. Front-End envia requisição para `/api/predict`
3. API processa dados
4. Modelo calcula probabilidade
5. Resultado é retornado
6. Interface exibe:
   - Cliente irá evadir ou não
   - Probabilidade associada

---

# 🗂 Estrutura Geral do Projeto

- 📊 Machine Learning (Python / Colab)
- 🔌 API Back-End (Spring Boot)
- 🌐 Página Web
- 📦 Modelo serializado (.pkl)
- 📋 Gestão via Trello

---

# 🔗 Links Importantes

📌 Trello (Gestão do Projeto)  
https://trello.com/invite/b/693b6cf8dcbc80d4150c9c55/ATTIf71c1cf1cafd8f24f32c18cc6407d976A6BB6107/hackathon-one-ii-brasil

📊 Dataset Kaggle  
https://www.kaggle.com/datasets/blastchar/telco-customer-churn

📓 Notebook Colab  
https://colab.research.google.com/drive/1EUFYp7s24ephprvisJaM1wlIQomyGgtM#scrollTo=l3TcR2Ecva9L

---

# 🏁 Conclusão

Este projeto entrega um **MVP completo de previsão de churn(evasão) de clientes**, integrando:

- Engenharia de Dados
- Machine Learning
- Desenvolvimento Back-End
- Desenvolvimento Front-End
- Integração via API

A solução demonstra capacidade de construção de sistemas **end-to-end**, aplicando boas práticas de modelagem, arquitetura e integração de serviços.

---

🚀 Projeto desenvolvido no Hackathon ONE II – Brasil
