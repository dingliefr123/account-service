package account.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.http.HttpResponse;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class  CustomForbiddenException extends RuntimeException {
  public String error = "Forbidden";
  public CustomForbiddenException(String message) {
    super(message);
  }
  public CustomForbiddenException(String message, String error) {
    super(message);
    this.error = error;
  }
}