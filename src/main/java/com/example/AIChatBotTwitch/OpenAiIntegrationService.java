package com.example.AIChatBotTwitch;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenAiIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiIntegrationService.class);

    private final ChatClient chatClient;

    public OpenAiIntegrationService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Generates an AI response based on user input from Twitch chat.
     * @param username The Twitch username of the requester
     * @param userMessage The message content from the user
     * @param botUsername The bot's username
     * @return AI-generated response formatted for Twitch chat
     */
    public String generateAiResponse(String username, String userMessage, String botUsername) {

        //Prompt creation, using PromptTemplate
        //User Message
        String userText = """ 
            The Twitch user {username} asks: {message}.
            """;
        PromptTemplate promptTemplate = new PromptTemplate(userText);
        Message userMessage2 = promptTemplate.createMessage(Map.of("username", username, "message", userMessage));

        //System Message
        String systemPrompt = """
          You are a twitch chatbot that entertain users and answers their questions.
          Your name is {name}.
          Keep responses concise (max around 256 characters) and in a single paragraph.
          """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", botUsername));

        //Prompt instantiation using userMessage and systemMessage
        Prompt prompt = new Prompt(List.of(userMessage2, systemMessage));

        //get response from AI model and return as a String
        try {
            return chatClient.prompt(prompt).call().content();
        } catch (Exception e) {
            log.error("Error generating AI response", e);
            return "Sorry, I couldn't process your request at the moment.";
        }
    }

    // *
    // * Alternative way for prompting and getting the response
    // *
    // //prompting the AI and getting the response
    // //system prompt
    // String systemPrompt = "You are a helpful and entertaining Twitch chat assistant. " +
    //         "Your name is " + botUsername + ". " +
    //         "Keep responses concise (max 200 characters) and in a single paragraph. ";
    //
    // //create the user message including context about who is asking
    // UserMessage userMessage = new UserMessage("The Twitch user '" + username + "' asks: " + message);
    //
    // //get response from OpenAI
    // ChatResponse response = chatClient.prompt()
    //         .user(userMessage.getText())
    //         .system(systemPrompt)
    //         .options(options)//system prompt here?*
    //         .call()
    //         .chatResponse(); // or .content() to get a String
    //
    // //get response in string
    // String r = response.getResults().getFirst().getOutput().getText();
    // *

}



