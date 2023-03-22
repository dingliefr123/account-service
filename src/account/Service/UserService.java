package account.Service;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.Entities.UserEntity;
import account.Exception.BadRequestException;
import account.Repository.UserRepository;
import account.Util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

}
