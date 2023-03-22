package account.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class StandardError {
  private LocalDateTime timestamp = LocalDateTime.now();
  private Integer status;
  private String message;
  private String path;
  private String error;

  public StandardError(HttpStatus status, String message, String path, String error) {
    this.status = status.value();
    this.message = message;
    this.path = path;
    this.error = error;
  }
}
