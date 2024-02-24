package com.konojoao.bottele.repository;

import com.konojoao.bottele.models.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Integer> {
    @Query(value = "SELECT * FROM chat_user where name = ?", nativeQuery = true)
    public ChatUser findByName(String name);

    @Query(value = "SELECT * FROM chat_user where telegramid = ?", nativeQuery = true)
    public Optional<ChatUser> findByTelegramId(String telegramId);

}
