# Twitch AI Chatbot
This project is a Twitch AI chatbot built with Spring Boot, Spring AI, and Twitch4J. The bot listens to  Twitch chat messages that mention it (using a format like `@YourBotName <message>`) and responds with AI-generated answers.

## Features
- **Twitch Chat Integration:**  
  Uses Twitch4J to connect to a Twitch channel and listen for messages that mention the bot.
- **AI-Powered Responses:**  
  Utilizes Spring AI (integrated with OpenAI) to generate responses using configurable models, temperature, and token limits.

## Prerequisites
- **Java 17+** (Java 21 recommended)
- **Maven or Gradle**
- **Spring Boot** (version 3.x)
- **Twitch Account** with a valid OAuth token, Client ID and Client Secret
- **OpenAI API Key**

## Installation & Setup
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/twitch-ai-chatbot.git
   cd twitch-ai-chatbot