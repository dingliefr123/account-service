package account.DTO;

import account.Entities.UserEntity;
import account.Entities.UserRoleEntity;
import account.Security.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleUserWithRoleResponse {
  private String name;
  private String lastname;
  private String email;
  private Long id;
  private List<Role> roles;

  @JsonProperty("roles")
  public List<String> formatRoles() {
    return roles
            .stream()
            .map(Role::Serialize)
            .collect(Collectors.toList())
            .stream()
            .sorted()
            .collect(Collectors.toList());
  }

  public static SingleUserWithRoleResponse fromUserEntity (UserEntity userEntity) {
    List<Role> roles = userEntity
            .getUserRoles()
            .stream()
            .map(UserRoleEntity::getRole)
            .collect(Collectors.toList());
    return SingleUserWithRoleResponse.builder()
            .name(userEntity.getName())
            .lastname(userEntity.getLastname())
            .email(userEntity.getEmail().toLowerCase())
            .id(userEntity.getId())
            .roles(roles)
            .build();
  }
}
