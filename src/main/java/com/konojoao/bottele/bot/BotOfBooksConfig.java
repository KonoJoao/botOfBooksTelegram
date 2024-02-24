package com.konojoao.bottele.bot;

import com.konojoao.bottele.config.BotConfig;
import com.konojoao.bottele.controllers.BotOfBooksController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Configuration
public class BotOfBooksConfig extends TelegramLongPollingBot {

    private BotOfBooksController botOfBooksController;
    private static final String botToken = BotConfig.BOT_TOKEN;
    private static final String botName = BotConfig.BOT_NAME;

    @Autowired
    public BotOfBooksConfig(BotOfBooksController botOfBooksController){
        this.botOfBooksController = botOfBooksController;
    }
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage messageResponse = new SendMessage();
        if(update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                Document document = update.getMessage().getDocument();
                Boolean flagAccountCreated = botOfBooksController.verifyAccountToUploadArchive(update.getMessage().getFrom());
                if(flagAccountCreated) {
                     messageResponse.setChatId(update.getMessage().getChatId());
                    if(document.getFileName().endsWith(".pdf") || document.getFileName().endsWith(".epub")) {
                        try {
                            GetFile getFile = new GetFile();
                            getFile.setFileId(document.getFileId());
                            String filePath = execute(getFile).getFilePath();
                            String fileName = botOfBooksController.getFileName(document.getFileName());
                            File file = downloadFile(filePath, new File("../archives/" + fileName));
                            messageResponse.setText("Arquivo " + fileName + " baixado com sucesso");
                            botOfBooksController.addBookToDatabase(update.getMessage().getFrom(), fileName);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    } else {
                        messageResponse.setText("Somente arquivos pdf ou mobi são aceitos");
                        messageResponse.setChatId(update.getMessage().getChatId());
                    }
                } else {
                    messageResponse.setText("Digite /start antes de enviar arquivos");
                    messageResponse.setChatId(update.getMessage().getChatId());
                }
            } else {
               messageResponse = botOfBooksController.onUpdateReceived(update);
            }
            try {
                execute(messageResponse);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
