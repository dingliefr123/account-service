package account.Service;

import account.DTO.GetSalaryResponse;
import account.DTO.SignUpResponse;
import account.DTO.SingleSalaryDTO;
import account.Entities.SalaryEntity;
import account.Entities.UserEntity;
import account.Exception.BadRequestException;
import account.Repository.SalaryRepository;
import account.Repository.UserRepository;
import account.Util.AuthUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log
public class SalaryService {
  @Autowired
  SalaryRepository salaryRepository;

  @Autowired
  UserRepository userRepository;

  @Transactional
  public void postSalary(List<SingleSalaryDTO> dtoList) {
    dtoList.forEach(dto -> {
      UserEntity userEntity =
              checkEmployeeExistsAndNoSalaryInPeriod(dto.getEmployee(), dto.getPeriod());
      SalaryEntity salaryEntity = SalaryEntity.fromSalaryDTO(dto);
      userEntity.addSalary(salaryEntity);
      userRepository.save(userEntity);
    });

  }

  @Transactional
  public void putSalary(SingleSalaryDTO dto) {
    var salaryEntity = checkSingleSalary(dto.getEmployee(), dto.getPeriod());
    salaryEntity.setAmount(dto.getSalary());
    salaryRepository.save(salaryEntity);
  }

  private UserEntity checkEmployeeExistsAndNoSalaryInPeriod(String email, String period) {
    var optional = userRepository
             .findTopDistinctByEmailIgnoreCase(email);
    UserEntity userEntity = optional.orElseThrow(() -> new BadRequestException(
             String.format("employee with the mail of %s not exists", email)));
    Optional<SalaryEntity> optionalSalary = salaryRepository
            .findTopDistinctByPeriodAndEmailIgnoreCase(period, email);
    if (optionalSalary.isPresent())
      throw new BadRequestException(
            String.format("%s's salary in %s already exists", email, period));
    return userEntity;
  }

  private SalaryEntity checkSingleSalary(String email, String period) {
    Optional<SalaryEntity> optional = salaryRepository
            .findTopDistinctByPeriodAndEmailIgnoreCase(period, email);
    return optional.orElseThrow(() ->
            new BadRequestException(
                    String.format("unable to find %s's salary in %s", email, period))
    );
  }

  public Object findSalariesByEmail(String period) {
    SignUpResponse withId = AuthUtil.checkAuthAndGetUserInfo();
    String email = withId.getEmail();
    if (StringUtils.isEmpty(period)) { // query all salaries
      var salaries = salaryRepository
              .findAllByEmailIgnoreCase(email, Sort.by("id").descending());
      return salaries.stream()
              .filter(Objects::nonNull)
              .map(ele -> GetSalaryResponse.fromSalaryEntity(ele, withId))
              .collect(Collectors.toList());
    }
    // query single salary
    Optional<SalaryEntity> salary = salaryRepository
            .findTopDistinctByPeriodAndEmailIgnoreCase(period, email);
    return salary.isPresent() ? salary
            .map(item -> GetSalaryResponse.fromSalaryEntity(item, withId)) :
            Collections.emptyMap();

  }
}
