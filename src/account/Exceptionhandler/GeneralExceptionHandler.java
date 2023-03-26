package account.Exceptionhandler;

import account.DTO.StandardError;
import account.Exception.BadRequestException;
import account.Exception.CustomForbiddenException;
import account.Exception.UnauthorizedException;
import account.Exception.UserNotFoundException;
import lombok.extern.java.Log;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@ControllerAdvice
@Log
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({
          ConstraintViolationException.class,
          CustomForbiddenException.class,
          UserNotFoundException.class
  })
  ResponseEntity<Object> handleConstraintViolationException(RuntimeException e) {
    String message = StringUtils.isEmpty(e.getMessage()) ?
            "Bad Request" : e.getMessage();
    String error = "Bad Request";
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    if (e.getClass() == CustomForbiddenException.class) {
      error = "Forbidden";
      httpStatus = HttpStatus.FORBIDDEN;
    } else if (e.getClass() == UserNotFoundException.class) {
      error = ((UserNotFoundException) e).error;
      httpStatus = UserNotFoundException.STATUS;
    }
    return new ResponseEntity<>(
            new StandardError(httpStatus, message, error), httpStatus);
  }

  @ExceptionHandler({ BadRequestException.class })
  protected ResponseEntity<Object> handleBadRequestException(
          BadRequestException ex,
          WebRequest request
  ) {
    var path = request.getDescription(false)
            .replace("uri=", "");
    var body = new StandardError(BadRequestException.STATUS, ex.getMessage(), path, ex.error);
    return new ResponseEntity<>(body, BadRequestException.STATUS);
  }

  @ExceptionHandler(UnauthorizedException.class)
  protected ResponseEntity<Object> handleUnauthorizedException(
          UnauthorizedException ex) {
    log.info("UnauthorizedException  " + ex.getMessage());
    var status = HttpStatus.UNAUTHORIZED;
    var body = new StandardError(status, ex.getMessage(), "Unauthorized");
    return new ResponseEntity<>(body, status);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request) {
    var path = request.getDescription(false)
              .replace("uri=", "");
    String error = "Bad Request";
    BindingResult bindingResult = ex.getBindingResult();
    Optional<FieldError> optionalFieldErr = Optional.empty();
    if (bindingResult.hasFieldErrors("new_password")) {
      optionalFieldErr = Optional.ofNullable(
              bindingResult.getFieldError("new_password"));
    } else if (bindingResult.hasFieldErrors("password")) {
      optionalFieldErr = Optional.ofNullable(
              bindingResult.getFieldError("new_password"));
    }
    String message = optionalFieldErr
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElse(ex.getMessage());
    var body = new StandardError(status, message, path, error);
    return new ResponseEntity<>(body, headers, status);
  }

}
