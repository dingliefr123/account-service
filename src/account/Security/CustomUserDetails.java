package account.Security;

import account.DTO.SignUpResponse;
import account.Entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
  private static final short MAX_ALLOWED_WRONG_LOGIN = 5;

  private final String username;
  private final String password;
  private final SignUpResponse dtoWithId;
  private final List<GrantedAuthority> rolesAndAuthorities;

  public CustomUserDetails(User user, SignUpResponse dtoWithId) {
    username = user.getUsername();
    password = user.getPassword();
    this.dtoWithId = dtoWithId;
    rolesAndAuthorities = new ArrayList<>(user.getAuthorities());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return rolesAndAuthorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public SignUpResponse getDtoWithId() {
    return dtoWithId;
  }

  // 4 remaining methods that just return true
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !dtoWithId.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


  public static boolean IsAccountNonLocked(UserEntity userEntity) {
    return !userEntity.isLocked();
  }

  public static boolean IsBrutal(UserEntity userEntity) {
    return !userEntity.isLocked() &&
            userEntity.getWrongInputCnt() >= MAX_ALLOWED_WRONG_LOGIN - 1;
  }
}
