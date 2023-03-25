package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
  public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
  public String error = "Not Found";
  public UserNotFoundException(String message) {
    super(message);
  }
}
