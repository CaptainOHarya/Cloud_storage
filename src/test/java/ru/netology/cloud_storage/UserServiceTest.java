package ru.netology.cloud_storage;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.netology.cloud_storage.Service.UserService;
import ru.netology.cloud_storage.dto.requestDto.UserRequestDto;
import ru.netology.cloud_storage.dto.responseDto.UserResponseDto;
import ru.netology.cloud_storage.repository.UserRepository;

import java.util.Objects;

// import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CloudStorageApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Возвращает пользователя, который есть в базе")
    public void authenticate_UserIsValid_ReturnsValidUser() {
        // given
        String login = "Marina";
        String password = "mar11";
        String email = "marina000@gmail.com";

        // when
        var user = userRepository.findUserByLoginAndPassword(login, password);

        // then
        assertTrue(user.isPresent());
        assertEquals("Marina", user.get().getLogin());
        assertNotEquals("Elena", user.get().getLogin());
        assertEquals(email, user.get().getEmail());

    }

    @Test
    @DisplayName("Возвращает токен, для пользователя, который есть в базе")
    public void authenticate_UserIsValid_ReturnsAuthToken() {
        // given
        String login = "AlienKesha";
        String password = "alien1";

        // when
        UserResponseDto response = userService.authenticate(new UserRequestDto(login, password));

        // then
        assertNotNull(response);
        assertNotNull(Objects.requireNonNull(response).getAuthToken());
        assertNotEquals(0, response.getAuthToken().length());
    }

    @Test
    @DisplayName("Возвращает нулевой объект, при неправильном пароле")
    public void authenticate_UserIsNotValid_ReturnsNull() {
        // given
        String login = "ErikaKu";
        String password = "alien1";

        // when
        UserResponseDto response = userService.authenticate(new UserRequestDto(login, password));
        assertNull(response);

    }

    @Test
    @DisplayName("Возвращает true при выходе пользователя")
    public void logout_UserIsOut_ReturnsTrue() {
        String login = "AlienKesha";
        String password = "alien1";

        // when
        UserResponseDto response = userService.authenticate(new UserRequestDto(login, password));
        String authToken = Objects.requireNonNull(response).getAuthToken();

        // then
        boolean actual = userService.logout(authToken);
        boolean expected = true;
        assertEquals(expected, actual);
    }

}
