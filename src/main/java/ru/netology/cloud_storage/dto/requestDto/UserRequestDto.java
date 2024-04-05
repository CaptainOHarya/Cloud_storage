package ru.netology.cloud_storage.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


// делаем запрос на frontend - получим ключ
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    @NotNull
    String login;
    @NotNull
    String password;
}
