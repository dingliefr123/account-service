package account.DTO;

import account.Entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @NotBlank
  private String lastname;

  @Email(regexp = "\\w+@acme.com")
  @NotNull
  private String email;

  @NotNull
  @NotBlank
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

}
