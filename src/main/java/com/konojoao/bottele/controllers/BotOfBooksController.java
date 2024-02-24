package com.konojoao.bottele.controllers;

import com.konojoao.bottele.commands.ChatCommands;
import org.springframework.stereotype.Controller;
import com.konojoao.bottele.services.BotOfBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Controller
public class BotOfBooksController {
    @Autowired
    private BotOfBooksService botOfBooksService;

    public SendMessage onUpdateReceived(Update update) {
        SendMessage messageResponse = new SendMessage();
        Message messageRequest = update.getMessage();
            if (messageRequest.getText().startsWith(ChatCommands.start)) {
                messageResponse = botOfBooksService.start(messageRequest.getFrom());
            } else if (messageRequest.getText().startsWith(ChatCommands.help)) {
                messageResponse = botOfBooksService.help(messageRequest.getFrom());
            } else if (messageRequest.getText().startsWith(ChatCommands.setEmail)) {
                messageResponse = botOfBooksService.email(messageRequest);
            } else if (messageRequest.getText().startsWith(ChatCommands.send)) {
                messageResponse = botOfBooksService.send(messageRequest);
            } else {
                messageResponse = botOfBooksService.defaultResponse(messageRequest.getFrom());
            }
        return messageResponse;
    }

    public Boolean verifyAccountToUploadArchive(User user){
      return botOfBooksService.verifyAccountToUploadArchive(user);
    }

    public void addBookToDatabase(User user, String book){
        botOfBooksService.addBookToDatabase(user, book);
    }

    public String getFileName(String fileName){
        return botOfBooksService.getFileName(fileName);
    }

}
