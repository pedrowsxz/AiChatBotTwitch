package com.example.AIChatBotTwitch.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiIntegrationService {

    private final ChatClient chatClient;

    public AiIntegrationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Generates an AI response based on user input from Twitch chat.
     * @param username The Twitch username of the requester
     * @param userMessage The message content from the user
     * @param botUsername The bot's username
     * @return AI-generated response formatted for Twitch chat
     */
    public String generateAiResponse(String username, String userMessage, String botUsername) {
        //Prompt creation, using the PromptTemplate class
        //creation of User Message
        String userText = """ 
            The Twitch user {username} asks: {message}.
            """;
        PromptTemplate promptTemplate = new PromptTemplate(userText);
        Message userMessage2 = promptTemplate.createMessage(Map.of("username", username, "message", userMessage));

        //creation of System Message
        String systemPrompt = """
          You are a twitch chatbot that entertain users and answers their questions.
          Your name is {name}.
          Keep responses concise (max around 256 characters) and in a single paragraph.
          """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", botUsername));

        //create a Prompt object by passing a list containing the userMessage and systemMessage
        Prompt prompt = new Prompt(List.of(userMessage2, systemMessage));

        //get response from AI model and return it as a String
        try {
            return chatClient.prompt(prompt).call().content();
        } catch (Exception e) {
            log.error("Error generating AI response", e);
            return "Sorry, I couldn't process your request at the moment.";
        }
    }
}



