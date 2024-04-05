package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloud_storage.entity.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    // метод поиска всех файлов, загруженных пользователем
    List<File>  findFilesByUserId(Long userId);
    // найти файл по Id пользователя и имени файла
    Optional<File> findFileByUserIdAndFileName(Long userId, String fileName);

    // найти все файлы по имени и фамилии пользователя
    List<File> findAllFilesByUserNameAndUserSurname(String name, String surname);
    // найти все файлы по номеру телефона пользователя
    List<File> findAllFilesByUserPhoneNumber(String phoneNumber);
    // найти все файлы по электронной почте пользователя
    List<File> findAllFilesByUserEmail(String email);
    // найти файл по имени
    Optional<File> findFileByFileName(String fileName);

   }
