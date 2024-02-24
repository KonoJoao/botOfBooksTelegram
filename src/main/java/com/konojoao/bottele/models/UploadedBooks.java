package com.konojoao.bottele.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UploadedBooks {

    @Id
    private BookAndUser bookAndUser;

    public BookAndUser getBookAndUser() {
        return bookAndUser;
    }

    public void setBookAndUser(BookAndUser bookAndUser) {
        this.bookAndUser = bookAndUser;
    }
}
