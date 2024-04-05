package ru.netology.cloud_storage.Service;

import org.springframework.stereotype.Service;
import ru.netology.cloud_storage.dto.requestDto.UserRequestDto;
import ru.netology.cloud_storage.dto.responseDto.UserResponseDto;
import ru.netology.cloud_storage.factory.UserRequestDtoFactory;
import ru.netology.cloud_storage.model.UserSession;

// логика аутентификации и выхода из приложения
// используем интерфейс т.к. потом удобно если что-то будет меняться
@Service
public interface UserService {
    UserResponseDto authenticate(UserRequestDto userRequestDto);
    boolean logout(String authToken);


    UserSession getSession(String authToken);
}
