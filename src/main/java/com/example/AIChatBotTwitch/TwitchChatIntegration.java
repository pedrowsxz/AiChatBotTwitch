package com.example.AIChatBotTwitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwitchChatIntegration implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TwitchChatIntegration.class);

    @Value("${twitch.channel}")
    private String channel;
    @Value("${twitch.bot.username}")
    private String botUsername;

    private final TwitchClient twitchClient;
    private final OpenAiIntegrationService openAiIntegrationService;

    public TwitchChatIntegration(TwitchClient twitchClient, OpenAiIntegrationService openAiIntegrationService) {
        this.twitchClient = twitchClient;
        this.openAiIntegrationService = openAiIntegrationService;
    }

    @Override
    public void run(String... args) throws Exception {
        //connect to twitch channel
        twitchClient.getChat().joinChannel(channel);
        log.info("Connected to channel: {}", channel);

        //register event listener for channel messages
        twitchClient.getEventManager().onEvent((ChannelMessageEvent.class), event -> {
            String message = event.getMessage();
            String username = event.getUser().getName();
            String botMention = "@" + botUsername.toLowerCase();

            //check if message mentioned the bot
            if (message.startsWith("@" + botUsername)) {
                String userMessage = message.substring(botMention.length()).trim();
                log.info("Received message from {}: {}", username, message);

                //get an AI-generated response
                String aiResponse = openAiIntegrationService.generateAiResponse(username, userMessage, botUsername);

                //format the reply: mention the user and include the AI response.
                String formattedResponse = String.format("@%s %s", event.getUser().getName(), aiResponse);

                //send the response to the Twitch chat.
                twitchClient.getChat().sendMessage(channel, formattedResponse);
            }
        });
    }
}
