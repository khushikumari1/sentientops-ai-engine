# 🚀 SentientOps AI Engine

### Autonomous Incident Analysis & Intelligent Ops Platform

---

## 🧠 Overview

**SentientOps AI Engine** is an AI-powered incident management system that analyzes system logs, identifies root causes, suggests fixes, and continuously learns from past incidents using graph-based memory.

Designed for modern distributed systems, it combines **AI + Observability + Automation** to assist SRE and DevOps workflows.

---

## 🔥 Key Features

* 🤖 AI-powered root cause analysis using Groq LLM
* 🧠 Graph-based learning with Neo4j (incident memory)
* 📊 Incident ingestion and tracking (PostgreSQL)
* 🔐 Secure APIs with JWT authentication
* ⚡ Real-time log analysis
* 📈 Failure prediction engine
* ⚙️ Kubernetes recovery actions *(Work in Progress)*

---

## 🏗️ Architecture

```
Incident Logs → AI Analysis → Root Cause → Suggested Fix
        ↓                         ↓
   PostgreSQL              Neo4j Memory Graph
```

---

## 🧪 API Endpoints

### 🔐 Authentication

* `POST /api/auth/signup` → Register user
* `POST /api/auth/login` → Get JWT token

---

### 📊 Incidents

* `POST /api/incidents` → Create incident
* `GET /api/incidents` → Get all incidents
* `GET /api/incidents/unresolved` → Get unresolved incidents
* `GET /api/incidents/service/{serviceName}` → Get by service

---

### 🤖 AI Reasoning

* `POST /api/ai/analyze` → Analyze logs using AI

---

### 📈 Predictions

* `GET /api/predict/failures` → Predict potential failures

---

### ⚙️ Actions (WIP)

* `POST /api/actions/execute` → Kubernetes-based recovery actions *(currently mocked)*

---

## 🧪 Sample Request

### AI Analysis

```json
POST /api/ai/analyze

{
  "logs": "Kafka consumer lag increasing. Consumer group not keeping up with producer rate."
}
```

---

## 🐳 Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/sentientops-ai-engine.git
cd sentientops-ai-engine
```

---

### 2. Set environment variables

Create a `.env` file:

```
GROQ_API_KEY=your_api_key_here
```

---

### 3. Run the application

```bash
docker-compose up --build
```

---

### 4. Access Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Authentication Flow

1. Register → `/api/auth/signup`
2. Login → `/api/auth/login`
3. Use JWT token:

```
Authorization: Bearer <your_token>
```

---

## 🧠 Tech Stack

* **Backend**: Spring Boot (Java)
* **AI**: Groq LLM API
* **Database**: PostgreSQL
* **Graph DB**: Neo4j
* **Security**: Spring Security + JWT
* **Containerization**: Docker

---

## 🚧 Future Improvements

* Full Kubernetes integration for automated recovery
* Frontend dashboard (React / Streamlit)
* Incident similarity search using Neo4j
* Auto-remediation based on AI confidence

---

## 👩‍💻 Author

**Khushi Kumari**

---

## ⭐ Vision

To build an **autonomous incident response system** that can detect, analyze, learn, and eventually resolve failures without human intervention.

---
