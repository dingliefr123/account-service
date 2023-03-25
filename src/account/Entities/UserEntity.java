package account.Entities;

import account.DTO.SignUpDTO;
import account.Security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "password")
  private String password;

  @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private List<UserRoleEntity> userRoles = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private List<SalaryEntity> salaries = new ArrayList<>();

  public void addSalary(SalaryEntity salaryEntity) {
    salaries.add(salaryEntity);
  }

  public void addUserRole(UserRoleEntity role) {
    if (Objects.isNull(userRoles)) {
      setUserRoles(List.of(role));
      return;
    }
    userRoles.add(role);
  }

  public boolean IsADMIN() {
    return checkOwnRoleAndReturn(Role.ADMINISTRATOR).isPresent();
  }

  public Optional<UserRoleEntity> checkOwnRoleAndReturn(Role role) {
    if (Objects.nonNull(userRoles) && !userRoles.isEmpty()) {
      for(UserRoleEntity roleEntity : userRoles)
        if (roleEntity.getRole() == role) return Optional.of(roleEntity);
      return Optional.empty();
    }
    return Optional.empty();
  }

  public static UserEntity fromSignUpDTO(SignUpDTO signUpDTO, String hashedPwd) {
    return UserEntity.builder()
            .name(signUpDTO.getName())
            .lastname(signUpDTO.getLastname())
            .email(signUpDTO.getEmail())
            .password(hashedPwd)
            .build();
  }
}
