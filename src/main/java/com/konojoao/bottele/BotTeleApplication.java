package com.konojoao.bottele;

import com.konojoao.bottele.bot.BotOfBooksConfig;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BotTeleApplication {
    private static BotOfBooksConfig botOfBooksConfig = null;

    public BotTeleApplication(BotOfBooksConfig botOfBooksConfig){
        BotTeleApplication.botOfBooksConfig = botOfBooksConfig;
    }
    public static void main(String[] args) {
        SpringApplication.run(BotTeleApplication.class, args);

    }

    @PostConstruct
    public static void registerBot(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(botOfBooksConfig);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
