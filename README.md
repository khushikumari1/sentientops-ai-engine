# 🚀 SentientOps AI Engine

### Autonomous Incident Analysis & Intelligent Ops Platform

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-DB-4169E1?style=flat-square&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Neo4j-GraphDB-008CC1?style=flat-square&logo=neo4j&logoColor=white" />
  <img src="https://img.shields.io/badge/Groq-LLM-7B2FBE?style=flat-square" />
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Status-Active-22c55e?style=flat-square" />
</p>

<p align="center">
  <b>AI-powered system that detects, analyzes, learns, and suggests fixes for production incidents</b>
</p>

---

## 🧠 Overview

**SentientOps AI Engine** is an intelligent backend platform designed to assist SRE and DevOps teams by transforming raw production logs into actionable intelligence — identifying root causes, suggesting fixes, and learning from every incident.

Built to move toward **self-healing infrastructure**.

---

## 💡 Why SentientOps?

Modern systems generate massive volumes of logs — but lack intelligent interpretation. SentientOps bridges the gap between **observability and intelligence** by:

- Converting raw logs → structured insights
- Learning from past incidents via graph relationships
- Assisting engineers in faster, smarter debugging

---

## ⚡ Key Features

| Feature | Description |
|---|---|
| 🤖 AI Root Cause Analysis | Uses Groq LLM to interpret logs and generate structured insights |
| 🧠 Persistent Graph Memory | Stores incidents, causes, and fixes as relationships in Neo4j |
| 📊 Incident Management | Tracks system health, logs, severity, and resolution in PostgreSQL |
| 🔐 Secure API Layer | JWT-based authentication + role-based access control |
| 📈 Failure Prediction Engine | Detects potential issues from historical patterns |
| ⚙️ Remediation Engine *(WIP)* | Designed for Kubernetes-based automated recovery actions |

---

## 🏗️ Architecture

```
Incident Logs
      │
      ▼
AI Analysis (Groq LLM)
      │
      ▼
Root Cause + Suggested Fix
   │                    │
   ▼                    ▼
PostgreSQL          Neo4j Graph
(Incidents)          (Memory)
   │                    │
   └────────┬───────────┘
            ▼
  Future Predictions & Learning
```

**System flow:**
1. Incident is ingested
2. AI analyzes logs → generates root cause + fix
3. Results stored in PostgreSQL + Neo4j
4. Future incidents leverage past knowledge for faster resolution

---

## 📂 Project Structure

```
src/
 ├── ai/            # Groq LLM integration
 ├── controller/    # REST API endpoints
 ├── service/       # Business logic
 ├── repository/    # Database access layer
 ├── model/         # Entities & DTOs
 ├── security/      # JWT authentication
 └── memory/        # Neo4j graph learning
```

---

## 🧪 API Reference

### 🔐 Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/signup` | Register a new user |
| `POST` | `/api/auth/login` | Obtain JWT token |

### 📊 Incidents

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/incidents` | Create a new incident |
| `GET` | `/api/incidents` | List all incidents |
| `GET` | `/api/incidents/unresolved` | Get unresolved incidents |
| `GET` | `/api/incidents/service/{serviceName}` | Get incidents by service |

### 🤖 AI Analysis

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/ai/analyze` | Analyze logs using Groq LLM |

### 📈 Predictions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/predict/failures` | Predict potential failures |

### ⚙️ Actions *(WIP)*

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/actions/execute` | Trigger Kubernetes-based recovery *(currently mocked)* |

---

## 🧪 Example: AI Analysis

**Request**

```http
POST /api/ai/analyze
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

```json
{
  "logs": "Kafka consumer lag increasing. Consumer group not keeping up with producer rate."
}
```

**Response**

```json
{
  "rootCause": "Kafka consumer lag due to high producer rate",
  "suggestedFix": "Scale consumers or rebalance partitions",
  "confidenceScore": 0.95
}
```

---

## 🐳 Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/khushikumari1/sentientops-ai-engine.git
cd sentientops-ai-engine
```

### 2. Set environment variables

Create a `.env` file in the project root:

```env
GROQ_API_KEY=your_api_key_here
```

### 3. Start the full system

```bash
docker-compose up --build
```

This starts the API server, PostgreSQL, and Neo4j together.

### 4. Access Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Authentication Flow

```
1. POST /api/auth/signup   →  Register user
2. POST /api/auth/login    →  Receive JWT token
3. Add to all requests:

   Authorization: Bearer <your_token>
```

---

## 🧠 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.x (Java 17) |
| AI / LLM | Groq LLM |
| Relational DB | PostgreSQL |
| Graph DB | Neo4j |
| Security | Spring Security + JWT |
| DevOps | Docker / Docker Compose |

---

## 💡 Design Philosophy

> Separate intelligence (AI) from execution (Kubernetes)

This ensures:
- **Modularity** — each component can evolve independently
- **Extensibility** — swap LLMs or databases without rewrites
- **Production readiness** — clean separation of concerns

---

## 🚀 What Makes This Unique?

- Combines **LLMs + Graph Databases + DevOps tooling** in one backend
- Goes beyond monitoring → **intelligent root cause reasoning**
- Introduces a **learning feedback loop** — the system gets smarter over time
- Designed like a **real production-grade backend**

---

## 🚧 Roadmap

- [ ] Full Kubernetes auto-remediation
- [ ] UI dashboard (React / Streamlit)
- [ ] Incident similarity search via Neo4j graph queries
- [ ] AI-driven automated resolution
- [ ] Continuous learning feedback loop

---

## 👩‍💻 Author

**Khushi Kumari**

---

## ⭐ Vision

To build an **autonomous SRE system** that not only detects failures — but **understands, learns, and resolves them intelligently**.
