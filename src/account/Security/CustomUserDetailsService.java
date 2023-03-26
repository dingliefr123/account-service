package account.Security;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.Entities.UserEntity;
import account.Entities.UserRoleEntity;
import account.Exception.UnauthorizedException;
import account.Repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  UserRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String userEmail) {
    Optional<UserEntity> optional =
            userRepo.findTopDistinctByEmailIgnoreCase(userEmail);

    if (optional.isEmpty()) {
      throw new UsernameNotFoundException("Not found: " + userEmail);
    }
    var userEntity = optional.get();
    if (userEntity.isLocked()) {
      throw new UnauthorizedException("User account is locked");
    }
    List<GrantedAuthority> authorities = userEntity
            .getUserRoles()
            .stream()
            .map(UserRoleEntity::getRole)
            .map(Role::Serialize)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    User user = new User(userEmail, userEntity.getPassword(), authorities);
    SignUpResponse withId = SignUpResponse.fromEntity(userEntity);
    return new CustomUserDetails(user, withId);
  }
}
