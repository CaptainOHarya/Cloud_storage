package ru.netology.cloud_storage.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.Service.FileService;
import ru.netology.cloud_storage.Service.UserService;
import ru.netology.cloud_storage.dto.requestDto.UserRequestDto;
import ru.netology.cloud_storage.dto.responseDto.UserResponseDto;
import ru.netology.cloud_storage.entity.File;
import ru.netology.cloud_storage.entity.User;
import ru.netology.cloud_storage.exception.CloudErrorOrInformation;
import ru.netology.cloud_storage.model.FileData;
import ru.netology.cloud_storage.repository.UserRepository;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/")
public class CloudController {
    final UserService userService;
    final FileService fileService;
    final UserRepository userRepository;


    @SneakyThrows
    @GetMapping("getAll")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.authenticate(userRequestDto);
        if (userResponseDto == null) {
            return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.UNAUTHORIZED.value(),
                    " Пользователь с таким логином не зарегистрирован или неправильный пароль "),
                    HttpStatus.UNAUTHORIZED);
            //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED.value())
        }
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        if (!userService.logout(authToken)) {
            return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.NOT_FOUND.value(),
                    "Пользователь с таким логином не найден"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") @NotNull String authToken,
                                        @RequestParam("filename") @NotNull String fileName,
                                        @RequestBody @NotNull MultipartFile file) {
        boolean answer = fileService.uploadFile(authToken, fileName, file.getContentType(), file.getSize(), file.getBytes());
        if (!answer) {
            return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.BAD_REQUEST.value(),
                    "Неправильный запрос"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.OK.value(),
                "Файл " + fileName + " успешно загружен"),
                HttpStatus.OK);

    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") @NotNull String authToken,
                                        @RequestParam("filename") @NotNull String filename) {
        String response = fileService.deleteFile(authToken, filename);
        return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.OK.value(),
                response),
                HttpStatus.OK);

    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestHeader("auth-token") @NotNull String authToken,
                                          @RequestParam("filename") @NotNull String filename) {
        ResponseEntity<byte[]> responseEntity;
        File file = fileService.getFile(authToken, filename);
        responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName()) // установим собственные заголовки
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getContent());
        return responseEntity;
    }

    @PutMapping("/file")
    public ResponseEntity<?> renameFile(@RequestHeader("auth-token") @NotNull String authToken,
                                        @RequestParam("filename") @NotNull String filename,
                                        @RequestParam("newFileName") @NotNull String newFilename) {
        boolean answer = fileService.renameFile(authToken, filename, newFilename);
        if (!answer) {
            return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.CONFLICT.value(),
                    "Файл с именем " + newFilename + " уже существует в базе данных"),
                    HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new CloudErrorOrInformation(HttpStatus.OK.value(),
                "Имя файла " + filename + " успешно заменено на " + newFilename),
                HttpStatus.OK);
    }

    @GetMapping("/list-files")
    public ResponseEntity<List<FileData>> getAllFiles(@RequestHeader("auth-token") @NotNull String authToken,
                                         @RequestParam("numberOfFiles") @NotNull int numberOfFiles) {
        List<FileData> fileDataList = fileService.getAllFiles(authToken, numberOfFiles);
        return new ResponseEntity<>(fileDataList, HttpStatus.OK);


        // return ResponseEntity.ok(fileDataList);
    }
/*
Здесь реализуем методы, написанные в протоколе:
- Авторизация;
- Выход из системы;
- Добавление файлов;
- Редактирование имени файла;
- Получение файла по Id;
- Удаление файла по Id;
- Получение списка файлов пользователя;
- Можно ещё поиск организовать по имени пользователя например...
 */


}
