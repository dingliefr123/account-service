package account.DTO;

import lombok.Data;

@Data
public class UpdatePasswordResponse {
  private final String status = "The password has been updated successfully";

  private String email;

  public UpdatePasswordResponse(String email) {
    this.email = email;
  }
}
