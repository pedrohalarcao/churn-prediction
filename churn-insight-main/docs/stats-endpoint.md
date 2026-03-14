# 📊 Stats Endpoint — Churn Insight

Este documento descreve o endpoint de estatísticas implementado na branch
`feature/stats-endpoint`.

O objetivo do endpoint é fornecer métricas internas simples da API,
permitindo validação de comportamento, contagem de requisições e análise
de erros durante o desenvolvimento.

---

## 🔗 Endpoint

```http
GET /api/stats
```

---

## 📥 Autenticação

🚧 **Atualmente não requer autenticação**
Este endpoint será protegido futuramente, quando a camada de autenticação
for implementada.

---

## 📤 Estrutura da Resposta

O endpoint retorna sempre o mesmo DTO (`StatsResponseDto`), com todos os
campos presentes.

```json
{
  "uptimeSeconds": number,
  "totalRequests": number,
  "predictionsSuccess": number,
  "validationErrors": number,
  "badJsonErrors": number,
  "modelServiceErrors": number,
  "internalErrors": number,
  "lastRequestAt": "ISO-8601 | null"
}
```

---

## 📑 Descrição dos Campos

| Campo                | Descrição                                         |
| -------------------- | ------------------------------------------------- |
| `uptimeSeconds`      | Tempo de execução da aplicação em segundos        |
| `totalRequests`      | Total de requisições HTTP contabilizadas pela API |
| `predictionsSuccess` | Total de previsões realizadas com sucesso         |
| `validationErrors`   | Quantidade de erros de validação (`400`)          |
| `badJsonErrors`      | JSON inválido ou enum inválido                    |
| `modelServiceErrors` | Erros retornados pelo serviço de ML               |
| `internalErrors`     | Erros internos inesperados (`500`)                |
| `lastRequestAt`      | Timestamp da última requisição contabilizada      |

---

## 🧪 Exemplo — Aplicação recém iniciada

```json
{
  "uptimeSeconds": 53,
  "totalRequests": 0,
  "predictionsSuccess": 0,
  "validationErrors": 0,
  "badJsonErrors": 0,
  "modelServiceErrors": 0,
  "internalErrors": 0,
  "lastRequestAt": null
}
```

---

## 🧪 Exemplo — Após erro de validação (`POST /api/predict`)

```json
{
  "uptimeSeconds": 368,
  "totalRequests": 3,
  "predictionsSuccess": 0,
  "validationErrors": 1,
  "badJsonErrors": 0,
  "modelServiceErrors": 0,
  "internalErrors": 0,
  "lastRequestAt": "2025-12-26T15:04:05.768662493-03:00"
}
```

---

## 🧪 Exemplo — Após rota inexistente (`/api/nao-existe`)

```json
{
  "uptimeSeconds": 515,
  "totalRequests": 5,
  "predictionsSuccess": 0,
  "validationErrors": 1,
  "badJsonErrors": 0,
  "modelServiceErrors": 0,
  "internalErrors": 0,
  "lastRequestAt": "2025-12-26T15:06:32.900387537-03:00"
}
```

---

## ⚙️ Regras de Contabilização

* Requisições **válidas** incrementam `totalRequests`
* Erros de validação incrementam `validationErrors`
* Erros `404` de API incrementam `totalRequests`
* Recursos estáticos (`/favicon.png`) **não são contabilizados**
* O próprio endpoint `/api/stats` **não incrementa métricas**
* As métricas são mantidas **em memória** e resetam ao reiniciar a aplicação

---

## 🧠 Implementação

* Contadores mantidos via `AtomicLongArray`
* Chaves definidas no enum `StatKey`
* Incrementação feita via `ApiStatsFilter`
* Sem acoplamento com Controllers

---



