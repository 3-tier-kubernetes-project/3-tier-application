package org.corodiak.dockerbackendsample.controller;

import org.corodiak.dockerbackendsample.type.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseModel handleEntity(Exception ex){
        return ResponseModel.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("데이터를 찾을 수 없습니다.")
                .build();
    }

    @ExceptionHandler({BadCredentialsException.class,IllegalArgumentException.class})
    public ResponseModel handleCredential(Exception ex){
        return ResponseModel.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("비밀번호가 일치하지 않습니다.")
                .build();
    }

}
