package ru.netology.cloud_storage.Service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage.entity.File;
import ru.netology.cloud_storage.entity.User;
import ru.netology.cloud_storage.exception.InputDataException;
import ru.netology.cloud_storage.exception.UserSessionException;
import ru.netology.cloud_storage.model.FileData;
import ru.netology.cloud_storage.model.UserSession;
import ru.netology.cloud_storage.repository.FileRepository;
import ru.netology.cloud_storage.repository.UserRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    UserService userService;
    FileRepository fileRepository;
    UserRepository userRepository;


    @Override
    public boolean uploadFile(String authToken, String fileName, String contentType, long size, byte[] bytes) {
        Long userId = verifyUser(authToken);
        File file;
        if (fileRepository.findFileByFileName(fileName).isEmpty()) {
            User user = userRepository.getReferenceById(userId);
            file = File.builder()
                    .fileName(fileName)
                    .fileType(contentType)
                    .content(bytes)
                    .size(size)
                    .user(user)
                    .build();
            fileRepository.save(file);
            log.info("Пользователь с id {} успешно загрузил файл {}", userId, fileName);
            return true;
        } else {
            log.error("Файл с именем {} уже существует", fileName);
            return false;

        }

    }

    @Override
    public String deleteFile(String authToken, String fileName) {
        String response;
        Long userId = verifyUser(authToken);
        File file = verifyFile(userId, fileName);
        fileRepository.deleteById(file.getId());
        response = " Файл " + fileName + " успешно удалён";
        log.info("Пользователь с id {} успешно удалил файл {}", userId, fileName);
        return response;
    }

    @Override
    public File getFile(String authToken, String fileName) {
        Long userId = verifyUser(authToken);
        File file = verifyFile(userId, fileName);
        log.info("Пользователь с id {} успешно получил файл {}", userId, fileName);
        return file;
    }

    @Override
    public boolean renameFile(String authToken, String fileName, String newFileName) {
        Long userId = verifyUser(authToken);
        File file = verifyFile(userId, fileName);
        if (fileRepository.findFileByUserIdAndFileName(userId, newFileName).isPresent()) {
            log.warn("Файл с таким именем уже существует в базе данных");
            return false;
        }  else {
            file.setFileName(newFileName);
            fileRepository.save(file);
            log.info("Пользователь с id {} успешно изменил имя файла {} на {}", userId, fileName, newFileName);
            return true;
        }

    }

    @Override
    public List<FileData> getAllFiles(String authToken, int numberOfFiles) {
        Long userId = verifyUser(authToken);
        if (numberOfFiles < 0) {
            log.warn("Количество файлов не может быть отрицательным");
            throw new InputDataException("Количество файлов не может быть отрицательным");
        }

        List<File> files = fileRepository.findFilesByUserId(userId);
        List<FileData> listFiles = files.stream()
                .limit(numberOfFiles)
                .map(file -> FileData.builder()
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .size(file.getSize())
                        .build()).collect(Collectors.toList());
        log.info("Был получен список файлов пользователя с id {} ", userId);

        return listFiles;
    }

    @SneakyThrows
    public Long verifyUser(String authToken) {
        UserSession session = userService.getSession(authToken);
        if (session == null) {
            log.error("Пользователь с таким логином не найден");
            throw new UserSessionException("Пользователь с таким логином не найден");
        }

        return Objects.requireNonNull(session).getUserId();

    }

    @SneakyThrows
    public File verifyFile(Long userId, String fileName) {
       var file = fileRepository.findFileByUserIdAndFileName(userId, fileName);
       if (file.isEmpty()) {
           log.error("Файл с именем " + fileName + " не найден");
           throw new FileNotFoundException("Файл с именем " + fileName + " не найден");
       }
       return file.get();

    }
}
