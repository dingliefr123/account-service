package account.DTO;

import account.Entities.UserEntity;
import account.Entities.UserRoleEntity;
import account.Security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
  private String name;
  private String lastname;
  private String email;
  private Long id;
  private List<Role> roles;
  @JsonIgnore
  private boolean locked;
  @JsonIgnore
  private int wrongInputCnt;

  @JsonProperty("roles")
  public List<String> formatRoles() {
    return roles
            .stream()
            .map(Role::Serialize)
            .collect(Collectors.toList());
  }

  public static SignUpResponse fromEntity(UserEntity userEntity) {
    List<Role> roles = userEntity
            .getUserRoles()
            .stream()
            .map(UserRoleEntity::getRole)
            .collect(Collectors.toList());
    return new SignUpResponse(
            userEntity.getName(),
            userEntity.getLastname(),
            userEntity.getEmail(),
            userEntity.getId(),
            roles,
            userEntity.isLocked(),
            userEntity.getWrongInputCnt());
  }

}