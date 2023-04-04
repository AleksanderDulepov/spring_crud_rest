package com.dulepov.spring.rest.exception_handling;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Метод, отрабатывающий при всевозможных исключениях в виде json
    @ExceptionHandler
    //ResponseEntity-класс, возвращающий ответ (в данном случае ввиде json)
    public ResponseEntity <EmployeeIncorrectData> handleException(Exception exception){
        EmployeeIncorrectData data=new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        //вернет json: {"info":"exception_message"}
    }

    //Метод, отрабатывающий при исключении NoSuchEmployeeException (вывод в виде json  (для READ ONE))
    @ExceptionHandler
    public ResponseEntity <EmployeeIncorrectData> handleException(NoSuchEmployeeException exception){
        EmployeeIncorrectData data=new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        //вернет json: {"info":"Работника с id = не существует"}
    }

    @ExceptionHandler
    //ResponseEntity-класс, возвращающий ответ (в данном случае ввиде json)
    public ResponseEntity <EmployeeIncorrectData> handleException(ValidationException exception){
        EmployeeIncorrectData data=new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        //вернет json: {"info":"exception_message"}
    }

}
