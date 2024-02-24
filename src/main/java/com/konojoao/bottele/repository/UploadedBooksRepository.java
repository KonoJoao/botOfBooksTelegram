package com.konojoao.bottele.repository;

import com.konojoao.bottele.models.BookAndUser;
import com.konojoao.bottele.models.UploadedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedBooksRepository extends JpaRepository<UploadedBooks, BookAndUser> {
}
