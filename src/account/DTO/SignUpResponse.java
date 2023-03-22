package account.DTO;

import account.Entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
  private String name;
  private String lastname;
  private String email;
  private Long id;

  public static SignUpResponse fromEntity(UserEntity userEntity) {
    return new SignUpResponse(
            userEntity.getName(),
            userEntity.getLastname(),
            userEntity.getEmail(),
            userEntity.getId());
  }

}