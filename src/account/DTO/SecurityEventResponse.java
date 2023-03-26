package account.DTO;

import account.Entities.EventEntity;
import account.Util.StringUtils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Data
public class SecurityEventResponse extends EventEntity {

  public static SecurityEventResponse fromEventEntity(EventEntity eventEntity) {
    String object = StringUtils.LowerUsernameInEmail(eventEntity.getObject());
    String subject = StringUtils.LowerUsernameInEmail(eventEntity.getSubject());
    final SecurityEventResponse response = new SecurityEventResponse();
    response.setDate(eventEntity.getDate());
    response.setId(eventEntity.getId());
    response.setObject(object);
    response.setSubject(subject);
    response.setAction(eventEntity.getAction());
    response.setPath(eventEntity.getPath());
    return response;
  }
}
