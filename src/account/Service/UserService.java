package account.Service;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.Entities.UserEntity;
import account.Exception.BadRequestException;
import account.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public SignUpResponse saveUser(SignUpDTO signUpDTO) {
    if (userRepository
            .findTopDistinctByEmailIgnoreCase(signUpDTO.getEmail())
            .isPresent()
    )
      throw new BadRequestException("User exist!", "Bad Request");
    var userEntity = UserEntity.fromSignUpDTO(signUpDTO);
    userRepository.save(userEntity);
    return SignUpResponse.fromEntity(userEntity);
  }

}
