package account.Service;

import account.DTO.PutRoleDTO;
import account.DTO.SecurityEventResponse;
import account.Entities.EventEntity;
import account.Repository.EventRepository;
import account.Security.Role;
import account.Security.SecurityEventType;
import account.Util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
  private static final String ANONYMOUS = "Anonymous";

  @Autowired
  EventRepository eventRepository;

  public List<SecurityEventResponse> getAllEvents() {
    return eventRepository
            .findAll()
            .stream()
            .map(SecurityEventResponse::fromEventEntity)
            .collect(Collectors.toList());
  }

  public void addLoginFailedEvent(String subject) {
    saveEventEntity(
            SecurityEventType.LOGIN_FAILED,
            subject,
            AuthUtil.GetCurrentUrl());
  }

  public void addCreateUserEvent(String object) {
    saveEventEntity(
            SecurityEventType.CREATE_USER,
            ANONYMOUS,
            object);
  }

  public void addDeleteUserEvent(String subject, String object) {
    saveEventEntity(
            SecurityEventType.DELETE_USER,
            subject,
            object);
  }

  public void addChangePasswordEvent(String subject, String object) {
    saveEventEntity(
            SecurityEventType.CHANGE_PASSWORD,
            subject,
            object);
  }

  public void addAccessDeniedEvent(String subject) {
    saveEventEntity(
            SecurityEventType.ACCESS_DENIED,
            subject,
            AuthUtil.GetCurrentUrl());
  }

  public void addLockEvent(String subject, String target) { addLockOrUnlockEvent(true, subject,target); }

  public void addUnLockEvent(String subject, String target) { addLockOrUnlockEvent(false, subject,target); }

  public void addGrantEvent(Role role, String target) {
    addGrantOrRemoveEvent(true, role,target);
  }

  public void addRemoveEvent(Role role, String target) {
    addGrantOrRemoveEvent(false, role,target);
  }

  public void addBrutalForceEvent(String subject) {
      String object = AuthUtil.GetCurrentUrl();
    saveEventEntity(
            SecurityEventType.BRUTE_FORCE,
            subject,
            object);
  }

  /* Internal functions */
  private void addLockOrUnlockEvent(boolean isLock, String subject, String target) {
    String object = GenerateLockOrUnlockObject(isLock, target);
    var action = isLock ?
            SecurityEventType.LOCK_USER : SecurityEventType.UNLOCK_USER;
    saveEventEntity(action, subject,object);
  }

  private void addGrantOrRemoveEvent(boolean isGrant, Role role, String target) {
    String object = GenerateGrantOrRemoveObject(isGrant, role, target);
    String subject = AuthUtil.GetCurrentAuthName();
    var action = isGrant ?
            SecurityEventType.GRANT_ROLE : SecurityEventType.REMOVE_ROLE;
    saveEventEntity(action, subject, object);
  }

  private void saveEventEntity (
          SecurityEventType actionType,
          String subject,
          String object) {
    String path = AuthUtil.GetCurrentUrl();
    LocalDateTime date = LocalDateTime.now();

    EventEntity entity = EventEntity.builder()
            .action(actionType)
            .path(path)
            .date(date)
            .subject(subject)
            .object(object)
            .build();
    eventRepository.save(entity);
  }

  private static String GenerateGrantOrRemoveObject(
          boolean isGrant,
          Role role,
          String target) {
    String operationTxt = isGrant ? "Grant" : "Remove";
    String ToOrFrom = isGrant ? "to" : "from";
    return String.format("%s role %s %s %s", operationTxt, role, ToOrFrom, target);
  }

  private static String GenerateLockOrUnlockObject(boolean isLock, String target) {
    String operationTxt = isLock ? "Lock" : "Unlock";
    return String.format("%s user %s", operationTxt, target);
  }
}
