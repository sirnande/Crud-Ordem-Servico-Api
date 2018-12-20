package com.para.crudos.api.setup;

import com.para.crudos.api.exceptions.ValidacaoException;
import com.para.crudos.api.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.*;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ ValidacaoException.class })
    @ResponseBody
    public ResponseEntity<Map<String, String>> exceptionHandler(ValidacaoException e) {

        Map<String, String> mapResposta = new HashMap<>();
        mapResposta.put("mensagemDoErroGerado", e.getMessage());

        return  ResponseEntity
                .badRequest()
                .body(mapResposta);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final Response errors = new Response();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.getErros().add(error.getDefaultMessage()));

        ex.getBindingResult()
                .getGlobalErrors()
                .forEach(error ->
                        errors.getErros().add(error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }


    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    public ResponseEntity<Response> exceptionHandler(ValidationException e) {
        Response<String> objectResponse = new Response<>();


        objectResponse.setErrors(Arrays.asList(e.getMessage()));

        return ResponseEntity
                .badRequest()
                .body(objectResponse);
    }

}
