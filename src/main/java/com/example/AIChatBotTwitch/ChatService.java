package com.example.AIChatBotTwitch;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;


//put configuration class on the same file for easyness of visualisation, will further move to its own classfile
@Configuration
public class AiConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    // Since the documentation on Spring AI/OpenAiApi was changed
    // I am currently reading and learning how to implement the new api

    OpenAiApi openAiApi = OpenAiApi.builder()
            .apiKey(openaiApiKey)
            .build();
    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(openaiApiKey);
    }

    //create a chat client with the custom OpenAiApi instance
    ChatClient chatClient = new ChatClient(openAiApi);
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("You are a helpful and entertaining Twitch chat assistant...") //system prompt here?*
                .build();
    }
}

@Service
public class ChatService {

    //injection of the ChatClient which its implementation is not completed yet
    private ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    //system prompt
    String systemPrompt = "You are a helpful and entertaining Twitch chat assistant. " +
            "Your name is " + botUsername + ". " +
            "Keep responses concise (max 200 characters) and in a single paragraph. " +
            "Be friendly and engaging.";

    //configure the OpenAI request with options
    OpenAiChatOptions options = OpenAiChatOptions.builder()
            .model("gpt-3.5-turbo")
            .temperature(0.7)
            .maxTokens(100)
            .build();

    //create the user message including context about who is asking
    UserMessage userMessage = new UserMessage("The Twitch user '" + username + "' asks: " + userQuery);

    //get response from OpenAI
    ChatResponse response = chatClient.prompt()
            .user(userMessage.getText())
            .system(systemPrompt) //system prompt here?*
            .call()
            .chatResponse();

    //ensure response is a single paragraph
    List<Generation> r = response.getResults();

    //will further create a method generateAiResponse, which have to return a String
}



