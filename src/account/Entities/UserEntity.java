package account.Entities;

import account.DTO.SignUpDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

  public static UserEntity fromSignUpDTO(SignUpDTO signUpDTO) {
    return UserEntity.builder()
            .name(signUpDTO.getName())
            .lastname(signUpDTO.getLastname())
            .email(signUpDTO.getEmail())
            .password(signUpDTO.getPassword())
            .build();
  }
}
