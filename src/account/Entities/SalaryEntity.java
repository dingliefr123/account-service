package account.Entities;

import account.DTO.SingleSalaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "salary")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryEntity {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "period")
  private String period;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "user_email", nullable = false, updatable = false)
  private String email;

  public static SalaryEntity fromSalaryDTO(SingleSalaryDTO dto) {
    return SalaryEntity.builder()
            .amount(dto.getSalary())
            .period(dto.getPeriod())
            .email(dto.getEmployee())
            .build();
  }
}
