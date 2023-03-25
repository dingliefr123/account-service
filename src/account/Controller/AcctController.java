package account.Controller;

import account.DTO.SingleSalaryDTO;
import account.Service.SalaryService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/acct")
@Validated
@Log
public class AcctController {
  @Autowired
  SalaryService salaryService;

  @PostMapping("/payments")
  Map<String, String> postMapping(
          @RequestBody
          @Size(min = 1)
          List<@Valid SingleSalaryDTO> dtoList) {
    salaryService.postSalary(dtoList);
    return POST_SUCCESS_RESPONSE;
  }

  @PutMapping("/payments")
  Map<String, String> putMapping(@Valid @RequestBody SingleSalaryDTO dto) {
    salaryService.putSalary(dto);
    return PUT_SUCCESS_RESPONSE;
  }

  private static final Map<String, String> POST_SUCCESS_RESPONSE =
          Map.of("status", "Added successfully!");

  private static final Map<String, String> PUT_SUCCESS_RESPONSE =
          Map.of("status", "Updated successfully!");

}
