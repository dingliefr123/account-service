package account.Exceptionhandler;

import account.DTO.StandardError;
import account.Exception.BadRequestException;
import account.Exception.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
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
          UnauthorizedException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request
  ) {
    var path = request.getDescription(false)
            .replace("uri=", "");
    var body = new StandardError(status, ex.getMessage(), path, "Unauthorized");
    return new ResponseEntity<>(body, headers, status);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request) {
    var path = request.getDescription(false)
              .replace("uri=", "");
    var body = new StandardError(status, ex.getMessage(), path, "Bad Request");
    return new ResponseEntity<>(body, headers, status);
  }

}
