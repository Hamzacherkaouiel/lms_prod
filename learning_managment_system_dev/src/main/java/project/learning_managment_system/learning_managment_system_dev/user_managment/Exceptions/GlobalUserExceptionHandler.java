package project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.InvalidUser;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.RoleNotFound;
import project.learning_managment_system.learning_managment_system_dev.user_managment.Exceptions.CustomesException.UserNotFound;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalUserExceptionHandler {
    @ExceptionHandler({InvalidUser.class})
    public ResponseEntity<String> handleUserCreationException(InvalidUser e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleRoleKeycloak(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler({RoleNotFound.class})
    public ResponseEntity<String> handleRoleException(RoleNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler({UserNotFound.class})
    public ResponseEntity<String> handleUserNotFoundException(UserNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccess(DataAccessException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the server");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in the server");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationInput(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
        String message = "ERROR: ";
        if (ex.getRootCause() != null && ex.getRootCause().getMessage() != null) {
            if (ex.getRootCause().getMessage().contains("email")) {
                message += "MAIL ALREADY EXIST";
            } else if (ex.getRootCause().getMessage().contains("password")) {
                message += "PASSWORD ALREADY EXIST";
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
