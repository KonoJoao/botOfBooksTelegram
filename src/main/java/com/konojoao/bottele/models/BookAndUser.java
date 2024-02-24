package com.konojoao.bottele.models;
import java.io.Serializable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class BookAndUser implements Serializable {
    private String book;
    @ManyToOne
    private ChatUser chatUser;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }
}
