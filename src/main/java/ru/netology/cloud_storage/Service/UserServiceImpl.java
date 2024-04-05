package ru.netology.cloud_storage.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage.entity.User;
import ru.netology.cloud_storage.model.UserSession;
import ru.netology.cloud_storage.dto.requestDto.UserRequestDto;
import ru.netology.cloud_storage.dto.responseDto.UserResponseDto;
import ru.netology.cloud_storage.repository.UserRepository;
import utils.UniqueIdentifier;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final ConcurrentMap<String, UserSession> userSessions;
    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userSessions = new ConcurrentHashMap<>();
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto authenticate(UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto;

        Optional<User> userFromDateBase = userRepository.findUserByLoginAndPassword(userRequestDto.getLogin(), userRequestDto.getPassword());
        if (userFromDateBase.isPresent()) {
            UserSession session = new UserSession(UniqueIdentifier.createUniqueID(), userFromDateBase.get().getId());
            userSessions.put(session.getUniqueSessionId(), session);
            userResponseDto = new UserResponseDto(session.getUniqueSessionId());
            log.info("Авторизация пользователя " + userRequestDto.getLogin() + " прошла успешно!!!");

        } else {
            log.error("Ошибка авторизации");
            userResponseDto = null;
        }
        return userResponseDto;

    }


    @Override
    public boolean logout(String authToken) {
        boolean marker;
        // сессия по умолчанию
        UserSession session = userSessions.getOrDefault(authToken, null);
        if (session != null) {
            userSessions.remove(session.getUniqueSessionId(), session);
            marker = true;
            log.info("Пользователь " + authToken + " вышел из сессии");


        } else {
            log.warn("В сессии нет такого пользователя");
            marker = false;
        }


        return marker;
    }

    public UserSession getSession(String authToken) {
        return userSessions.get(authToken);
    }


}
