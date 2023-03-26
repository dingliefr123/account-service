package account.Entities;

import account.Security.SecurityEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class EventEntity {
  @Id
  @GeneratedValue
  @Column(name = "eventid")
  private Long id;

  private LocalDateTime date;

  @Enumerated(EnumType.STRING)
  private SecurityEventType action;

  private String object;

  private String subject;

  private String path;

}
