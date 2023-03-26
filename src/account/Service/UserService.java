package account.Service;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.Entities.UserEntity;
import account.Entities.UserRoleEntity;
import account.Exception.BadRequestException;
import account.Exception.UserNotFoundException;
import account.Repository.UserRepository;
import account.Security.CustomUserDetails;
import account.Security.Role;
import account.Util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EventService eventService;

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
    // @TODO CREATE_USER
    eventService.addCreateUserEvent(signUpDTO.getEmail());
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
    // @TODO CHANGE_PASSWORD
    eventService.addChangePasswordEvent(email, email);
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
    // @TODO DELETE_USER
    eventService.addDeleteUserEvent(AuthUtil.GetCurrentAuthName(), email);
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

  public void unlockAndClearWrongCnt(String object) {
    userRepository.clearWrongCntByAndUlockEmail(object);
  }

  public void unlockAndClearWrongCntAndSaveEvent(String object) {
    userRepository.clearWrongCntByAndUlockEmail(object);
    // @TODO UNLOCK_USER
    eventService.addUnLockEvent(AuthUtil.GetCurrentAuthName(), object);
  }

  public void updateWrongCntByEmailAndSaveEvent(String subject) {
    // @TODO LOGIN_FAILED
    eventService.addLoginFailedEvent(subject);

    Optional<UserEntity> optional =
            userRepository.findTopDistinctByEmailIgnoreCase(subject);
    if (optional.isEmpty()) return;
    UserEntity userEntity = optional.get();
    if (CustomUserDetails.IsAccountNonLocked(userEntity))
      userRepository.updateWrongCntByEmail(subject);
    if (CustomUserDetails.IsBrutal(userEntity)) {
      // @TODO BRUTAL_FORCE
      eventService.addBrutalForceEvent(subject);
      lockUser(subject, subject);
    }
  }

  public void lockUser(String subject, String object) {
    if (getUser(object).IsADMIN())
      throw new BadRequestException("Can't lock the ADMINISTRATOR!");
    userRepository.lockUserByEmail(object);
    // @TODO LOCK_USER
    eventService.addLockEvent(subject, object);
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
