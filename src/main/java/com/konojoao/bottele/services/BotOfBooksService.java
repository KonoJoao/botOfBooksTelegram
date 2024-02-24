package com.konojoao.bottele.services;

import com.konojoao.bottele.models.BookAndUser;
import com.konojoao.bottele.models.ChatUser;
import com.konojoao.bottele.models.UploadedBooks;
import com.konojoao.bottele.repository.ChatUserRepository;
import com.konojoao.bottele.repository.UploadedBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.Optional;

@Service
public class BotOfBooksService {
    @Autowired
    private ChatUserRepository chatUserRepository;
    @Autowired
    private UploadedBooksRepository uploadedBooksRepository;
    @Autowired
    private SendEmailService sendEmailService;

    private void saveUser(User user){
        Optional<ChatUser> usuarioBanco = chatUserRepository.findByTelegramId(user.getId().toString());
        if(usuarioBanco.isEmpty()) {
            ChatUser chatUser = new ChatUser();
            chatUser.setName(user.getFirstName());
            chatUser.setTelegramID(user.getId().toString());
            chatUserRepository.save(chatUser);
        }
    }

    private void saveEmail(User user, String email){
        ChatUser chatUser = chatUserRepository.findByName(user.getFirstName());
        chatUser.setEmail(email);
        chatUserRepository.save(chatUser);
    }

    public SendMessage start(User user){
        saveUser(user);
        SendMessage messageResponse = new SendMessage();
        messageResponse.setText("Olá. Você está usando um bot de envio de livros para seu kindle. Digite /email para adicionar seu email destinatário");
        messageResponse.setChatId(user.getId());
        return messageResponse;
    }

    public SendMessage help(User user){
        saveUser(user);
        SendMessage messageResponse = new SendMessage();
        messageResponse.setText("Para adicionar seu email: /email <seu email>\npara enviar um livro para seu email: /send <arquivo de livro>\n");
        messageResponse.setChatId(user.getId());
        return messageResponse;
    }
    public SendMessage email(Message message){
        SendMessage messageResponse = new SendMessage();

        try {
            String email = message.getText().split(" ")[1];
            saveEmail(message.getFrom(), email);
            messageResponse.setText("Seu email: " + email + " foi adicionado.");
            messageResponse.setChatId(message.getFrom().getId());
        }
        catch(Exception e){
            messageResponse.setText("Houve um erro ao tentar adicionar seu email. Talvez você não tenha digitado nenhum");
            messageResponse.setChatId(message.getFrom().getId());
        }
        return messageResponse;
    }
    public SendMessage send(Message message){
        User user = message.getFrom();
        String book  = message.getText().split(" ")[1];
        SendMessage messageResponse = new SendMessage();
        messageResponse.setChatId(user.getId());
        try {
            Optional<ChatUser> chatUser = chatUserRepository.findByTelegramId(user.getId().toString());
            sendEmailService.sendEmail(chatUser.get().getEmail(), "Kindle book", "book delivered", book);
            messageResponse.setText("Seu livro foi enviado");
        } catch(Exception e){
            System.out.println(e);
            messageResponse.setText("Houve uma falha no envio de seu email");
        }
        return messageResponse;
    }

    public Boolean verifyAccountToUploadArchive(User user){
        Optional<ChatUser> usuarioBanco = chatUserRepository.findByTelegramId(user.getId().toString());
        if(usuarioBanco.isEmpty()) {
            return false;
        }
        return true;
    }

    public void addBookToDatabase(User user, String book){
        UploadedBooks uploadedBook = new UploadedBooks();
        BookAndUser bookAndUser = new BookAndUser();
        bookAndUser.setBook(book);
        bookAndUser.setChatUser(chatUserRepository.findByTelegramId(user.getId().toString()).get());
        uploadedBook.setBookAndUser(bookAndUser);
        uploadedBooksRepository.save(uploadedBook);
    }

    public SendMessage defaultResponse(User user){
        SendMessage messageResponse = new SendMessage();
        messageResponse.setText("Digite /start para iniciar o uso com o bot");
        messageResponse.setChatId(user.getId());
        return messageResponse;
    }

    public String getFileName(String fileName){
        String[] fileNameArray = fileName.split(" ");
        String fileNameFormated = "";
        for(String word : fileNameArray){
            fileNameFormated += word.substring(0,1).toUpperCase().concat(word.substring(1));;
        }
        return fileNameFormated;
    }
}
