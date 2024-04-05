package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloud_storage.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // найти пользователя по логину и паролю
    Optional<User> findUserByLoginAndPassword(String login, String password);

    // найти пользователя по электронной почте - просто ради интереса потом можно реализовать
    Optional<User> findUserByEmail(String email);

    // найти пользователя по номеру телефона - просто ради интереса потом можно реализовать
    Optional<User> findUserByPhoneNumber(String phoneNumber);


}
