package account.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDTO {
  @NotNull
  @NotBlank
  @Size(min = 12, message = "Password length must be 12 chars minimum!")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String new_password;
}
