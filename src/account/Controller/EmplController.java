package account.Controller;

import account.DTO.GetSalaryResponse;
import account.DTO.SignUpResponse;
import account.Exception.BadRequestException;
import account.Service.SalaryService;
import account.Util.AuthUtil;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empl")
@Log
public class EmplController {

  @Autowired
  SalaryService salaryService;

  @GetMapping("/payment")
  Object getPayment(
          @RequestParam(required = false) String period) {
    log.info("/payment entry " + period);
    boolean querySingle = !StringUtils.isEmpty(period);
    if (querySingle && !period.matches("(0[1-9]|1[012])-20([01]\\d|2[012])"))
      throw new BadRequestException("");
    var salaryListOrItem =
            salaryService.findSalariesByEmail(period);
    log.info("/payment close id:" + salaryListOrItem);
    return salaryListOrItem;
  }

}
