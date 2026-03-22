# 🏏 CricAI – AI Cricket Match Prediction App

CricAI is a full-stack AI-powered cricket prediction application that combines statistical models with an intelligent AI assistant to provide match insights and explanations.

---

## 🚀 Features

### 📊 Match Predictions

* Predicts match outcomes using:

  * ELO rating system
  * Recent team performance (win rate)
* Displays probability percentages
* Clean and modern UI

### 🤖 AI Match Assistant

* Chat-based assistant for match queries
* Context-aware (understands selected match)
* Answers:

  * Who will win?
  * Why will they win?
  * General cricket questions

### 🧠 Smart AI Agent

* JSON-based tool calling (stable & reliable)
* Autonomous decision-making
* Uses backend tools:

  * `predict_match`
  * `get_matches`

---

## 🏗️ Tech Stack

### Frontend

* React.js
* CSS
* Fetch API

### Backend

* Spring Boot
* REST APIs

### AI

* Ollama (Gemma model)
* Custom AI Agent

---

## 📂 Project Structure

```
CricAI/
├── frontend/   # React app
└── backend/    # Spring Boot app
```

---

## 🧠 How It Works

1. User selects a match
2. Chat assistant receives query
3. Match context is sent to backend
4. AI agent:

   * Decides which tool to call
   * Executes prediction logic
5. Returns prediction + explanation

---

## ✨ Example

```
User: Who will win?

Response:
Australia: 55.6%, South Africa: 44.4%

Australia has a higher chance due to better recent form and rating.
```

---

<img width="1918" height="912" alt="image" src="https://github.com/user-attachments/assets/b6d2e037-27a4-4838-a5a6-4fe0b2a62443" />

<img width="1918" height="906" alt="image" src="https://github.com/user-attachments/assets/eb9bbea3-6fb3-438c-b632-8db95b8d35be" />


## 🚀 Future Improvements

* Live match API integration
* Player-level analytics
* Chat memory
* Voice assistant

---

## 👨‍💻 Author

Hemanth Ramawat

Full Stack Developer (React + Spring Boot) | AI Enthusiast

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
