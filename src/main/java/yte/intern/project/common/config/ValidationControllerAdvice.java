package yte.intern.project.common.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;

@ControllerAdvice
public class ValidationControllerAdvice {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<MessageResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.ok(new MessageResponse(message, MessageType.ERROR));
    }
}
