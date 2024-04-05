package ru.netology.cloud_storage.factory;

import org.springframework.stereotype.Component;
import ru.netology.cloud_storage.dto.requestDto.UserRequestDto;
import ru.netology.cloud_storage.entity.User;
// данный класс занимается созданием DTO,
// потом будем инъектить этот бин в контроллер

// можно потом подумать над этим, использовать вместо UserReauestDto
// TODO - пока использование данного класса нецелесообразно
@Component
public class UserRequestDtoFactory {

    public UserRequestDto makeUserDto(User user) {

        return UserRequestDto.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .build();

    }

}
