/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.exceptions.handlers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.currency.exchange.exceptions.BussinesRuleException;

/**
 *
 * @author rober
 */
@RestControllerAdvice
public class ServiceExceptionHandler {

    @Autowired
    private MessageSource businessMessageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            errors.put(field, message);
        });
        return new ResponseEntity(createMessage(errors), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BussinesRuleException.class)
    public ResponseEntity<?> handleBussinesRuleException(BussinesRuleException ex) {
        Map<String, Object> errors = new HashMap<>();
        String msg = businessMessageSource.getMessage(ex.getMessage(), ex.getObjects(), LocaleContextHolder.getLocale());
        errors.put("message", msg);
        return new ResponseEntity(createMessage(errors), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> createMessage(Map<String, Object> messagesMap) {
        Map<String, Object> message = new HashMap<>();
        message.put("timestamp", Instant.now().toString());
        message.put("error", messagesMap);
        return message;
    }
}
