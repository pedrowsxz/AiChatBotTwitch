package com.example.AIChatBotTwitch;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    //ChatClient injection
    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generateAiResponse(String username, String userMessage, String botUsername) {

        //Configure the OpenAI request with options
        //Alternatively the options could be configured on application.properties file
        //Other alternative would not to configure this at all and use the default one
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.7)
                .maxTokens(100)
                .build();

        //Code for prompting, using PromptTemplate
        //User Message
        String userText = """ 
            The Twitch user {username} asks: {message}
            """;
        PromptTemplate promptTemplate = new PromptTemplate(userText);
        Message userMessage2 = promptTemplate.createMessage(Map.of("username", username, "message", userMessage));

        //System Message
        String systemPrompt = """
          You are a helpful AI assistant that helps people find information.
          Your name is {name}
          Keep responses concise (max 200 characters) and in a single paragraph.
          """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", botUsername));

        //Prompt instantiation using userMessage and systemMessage
        Prompt prompt = new Prompt(List.of(userMessage2, systemMessage));

        //get response from AI as a string
        String response = chatClient.prompt(prompt)
                .options(options)//system prompt here?*
                .call()
                .content();

        return response;
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



