# Twitch AI Chatbot
This project is a Twitch AI chatbot built with Spring Boot, Spring AI, and Twitch4J. The bot listens to  Twitch chat messages that mention it (using a format like `@YourBotName <message>`) and responds with AI-generated answers.

## Features
- **Twitch Chat Integration:**  
  Uses Twitch4J to connect to a Twitch channel and listen for messages that mention the bot.
- **AI-Powered Responses:**  
  Utilizes Spring AI (integrated with OpenAI) to generate responses using configurable models, temperature, and token limits.

## Prerequisites
- **Java 17+** (Java 21 recommended)
- **Maven**
- **Spring Boot** (version 3.x)

## Credentials
- **Twitch Account** with a valid:
  - **OAuth token**
  - **Client ID**
  - **Client Secret**


- **API Key for the LLM model you wish to use**
  - To configure the LLM model in the application, specify the model in the application.properties file.
  - Example Models: [OpenAI](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html), [Mistral](https://docs.spring.io/spring-ai/reference/api/chat/mistralai-chat.html), [DeepSeek](https://docs.spring.io/spring-ai/reference/api/chat/deepseek-chat.html), [Ollama](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)
  - For detailed instructions on how to configure and switch between each of these models and its options, refer to the [Spring AI documentation](https://docs.spring.io/spring-ai/reference/api/index.html).

## Installation & Setup
1. **Clone the Repository:**
   ```bash
    # Clone repository
    git clone https://github.com/yourusername/twitch-ai-chatbot.git
    cd twitch-ai-chatbot
   
    # Compile, clean any previous builds and packages the code into an executable .jar file
    ./mvnw clean package
    # Run the application
    java -jar target/twitch-ai-chatbot-0.0.1-SNAPSHOT.jar
   
    # Alternatively, compile and run in one step, without creating a .jar file
    mvn spring-boot:run
   ```