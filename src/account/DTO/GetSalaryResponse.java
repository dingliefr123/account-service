package account.DTO;

import account.Entities.SalaryEntity;
import account.Util.SalaryConvertor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSalaryResponse {

  private String name;
  private String lastname;
  private String period;

  private Long salary;

  private static final List<String> MonthStrings = Arrays
          .stream(Month.values())
          .map(Enum::name)
          .collect(Collectors.toList());

  @JsonProperty("salary")
  public String salaryFormatter() {
    return SalaryConvertor.serialize(salary);
  }

  @JsonProperty("period")
  public String periodFormatter() {
    int monthIdx = Integer.parseInt(period.substring(0,2)) - 1;
    String monthStr = MonthStrings
            .get(monthIdx)
            .toLowerCase();
    String firstUpperLetter = Character
            .toString(monthStr.charAt(0)).toUpperCase();
    monthStr = new StringBuffer(monthStr).replace(0, 1, firstUpperLetter).toString();
    return new StringBuffer(period)
            .replace(0, 2, monthStr).toString();
  }

  public static GetSalaryResponse fromSalaryEntity (SalaryEntity salaryEntity, SignUpResponse res) {
    return new GetSalaryResponse(
            res.getName(),
            res.getLastname(),
            salaryEntity.getPeriod(),
            salaryEntity.getAmount());
  }

}
