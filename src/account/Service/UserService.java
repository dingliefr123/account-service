package account.Service;

import account.Controller.AdminController;
import account.DTO.PutRoleDTO;
import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.DTO.SingleUserWithRoleResponse;
import account.Entities.SalaryEntity;
import account.Entities.UserEntity;
import account.Entities.UserRoleEntity;
import account.Exception.BadRequestException;
import account.Exception.CustomForbiddenException;
import account.Exception.UserNotFoundException;
import account.Repository.UserRepository;
import account.Security.Role;
import account.Util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Transactional
  public SignUpResponse saveUser(SignUpDTO signUpDTO) {
    final String rawPassword = signUpDTO.getPassword();
    AuthUtil.checkBreachedPwd(rawPassword);
    var optional = userRepository
            .findTopDistinctByEmailIgnoreCase(signUpDTO.getEmail());
    if (optional.isPresent())
      throw new BadRequestException("User exist!", "Bad Request");
    String hashed = passwordEncoder.encode(rawPassword);
    var userEntity = UserEntity.fromSignUpDTO(signUpDTO, hashed);
    setRoleEntity(userEntity);
    userRepository.save(userEntity);
    return SignUpResponse.fromEntity(userEntity);
  }

  @Transactional
  public SignUpResponse changePassword(String newPassword) {
    AuthUtil.checkBreachedPwd(newPassword);
    SignUpResponse userInfo = AuthUtil.checkAuthAndGetUserInfo();
    AuthUtil.checkCurAndNewPwdNotSame(newPassword, passwordEncoder);
    String email = userInfo.getEmail();
    String newHashedPwd = passwordEncoder.encode(newPassword);
    int affectedCnt = userRepository
            .updateUserPwdByEmail(newHashedPwd, email);
    if (affectedCnt < 1)
      throw new BadRequestException("");
    return userInfo;
  }

  public UserEntity getUser(String email) {
    Optional<UserEntity> optional = userRepository
            .findTopDistinctByEmailIgnoreCase(email);
    return optional
            .orElseThrow(() -> new UserNotFoundException("User not found!"));
  }

  @Transactional
  public void deleteUser(String email) {
    Optional<UserEntity> optional = userRepository
            .findTopDistinctByEmailIgnoreCase(email);
    UserEntity userEntity = optional
            .orElseThrow(() -> new UserNotFoundException("User not found!"));
    if (userEntity.IsADMIN())
      throw new BadRequestException("Can't remove ADMINISTRATOR role!");

    userRepository.delete(userEntity);
  }

  @Transactional
  public void addUserRole(UserEntity userEntity, Role role) {
    userEntity.addUserRole(UserRoleEntity.FromRole(role));
    userRepository.save(userEntity);
  }

  public List<UserEntity> getAllUsr() {
    return this.userRepository.findAll();
  }

  private void setRoleEntity(UserEntity userEntity) {
    UserRoleEntity roleEntity = UserRoleEntity.GetUserRole();
    if (checkIsFirstRegistered())
      roleEntity = UserRoleEntity.GetAdminRole();
    userEntity.addUserRole(roleEntity);
  }

  private boolean checkIsFirstRegistered() {
    return userRepository.count() == 0;
  }

}
