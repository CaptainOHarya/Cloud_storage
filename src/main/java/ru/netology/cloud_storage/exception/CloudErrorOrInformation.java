package ru.netology.cloud_storage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Благодаря этому классу будем получать информативный ответ с кодом ошибки или комментарием в формате JSON
@AllArgsConstructor
@Setter
@Getter
public class CloudErrorOrInformation {
    private int statusCode;
    private String message;


}
