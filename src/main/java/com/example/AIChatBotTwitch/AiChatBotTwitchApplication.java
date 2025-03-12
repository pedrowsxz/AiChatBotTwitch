package com.example.AIChatBotTwitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/*
*
* move what is inside CommandLineRunner to proper file
* properly connect and call the IA generation method
*
*/
@SpringBootApplication
@Slf4j
public class AiChatBotTwitchApplication {

	private static final Logger log = LoggerFactory.getLogger(AiChatBotTwitchApplication.class);

	@Value("${twitch.channel}")
	private String channel;

	@Value("${twitch.bot.username}")
	private String botUsername;

	public static void main(String[] args) {
		SpringApplication.run(AiChatBotTwitchApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(TwitchClient twitchClient) {
		return args -> {
			//connect to twitch channel
			twitchClient.getChat().joinChannel(channel);
			log.info("Connected to channel: {}", channel);

			//register event listener for channel messages
			twitchClient.getEventManager().onEvent((ChannelMessageEvent.class), event -> {
				String message = event.getMessage();
				String username = event.getUser().getName();

				if (message.startsWith("@" + botUsername)) {
					String userMessage = message.substring(botUsername.length() + 1).trim();
					log.info("Received message from {}: {}", username, message);

					//get an AI-generated response
					String aiResponse = ChatService.generateAiResponse(username, userMessage, botUsername);

					//format the reply: mention the user and include the AI response.
					String formattedResponse = String.format("@%s %s", event.getUser().getName(), aiResponse);

					//send the response to the Twitch chat.
					twitchClient.getChat().sendMessage(channel, formattedResponse);
				}
			});
        };
	}
}
