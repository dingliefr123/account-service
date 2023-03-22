package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
  public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
  public String error = "Bad Request";
  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, String error) {
    super(message);
    this.error = error;
  }
}
