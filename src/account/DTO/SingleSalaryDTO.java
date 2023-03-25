package account.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleSalaryDTO {
  @Email(regexp = "\\w+@acme.com")
  @NotNull
  private String employee;

  @NotNull
  @Pattern(regexp = "(0[1-9]|1[012])-20([01]\\d|2[012])")
  private String period;

  @NotNull
  @Min(0)
  private Long salary;

}
