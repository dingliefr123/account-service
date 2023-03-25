package account.Service;


import account.DTO.PutRoleDTO;
import account.DTO.SingleUserWithRoleResponse;
import account.Entities.UserEntity;
import account.Entities.UserRoleEntity;
import account.Exception.BadRequestException;
import account.Exception.UserNotFoundException;
import account.Repository.UserRepository;
import account.Repository.UserRoleRepository;
import account.Security.Role;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
public class UserRoleService {

  @Autowired
  UserRoleRepository userRoleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  public SingleUserWithRoleResponse putRole(PutRoleDTO dto) {
    PutRoleDTO.RoleOperation operation = dto.getRoleOperation();
    Role role = Role.fromString(dto.getRole());
    boolean isOperateAdmin = role.equals(Role.ADMINISTRATOR);
    if (isOperateAdmin) {
      if (operation.IS_REMOVE())
        throw new BadRequestException("Can't remove ADMINISTRATOR role!");
      else
        throw new BadRequestException("The user cannot combine administrative and business roles!");
    }
    UserEntity userEntity = userService.getUser(dto.getUser());
    if (userEntity.IsADMIN() && operation.IS_GRANT())
      throw new BadRequestException("The user cannot combine administrative and business roles!");
    if (operation.equals(PutRoleDTO.RoleOperation.REMOVE)) {
      removeUserRole(userEntity, role);
    } else {
      addUserRole(userEntity, role);
    }
    return SingleUserWithRoleResponse.fromUserEntity(userEntity);
  }

  public List<SingleUserWithRoleResponse> queryUsersWithRoleInfo() {
    return userService.getAllUsr()
            .stream()
            .map(SingleUserWithRoleResponse::fromUserEntity)
            .collect(Collectors.toList());
  }

  private void addUserRole(UserEntity userEntity, Role role) {
    if (userEntity.checkOwnRoleAndReturn(role).isPresent())
      throw new BadRequestException("Already Granted");
    userService.addUserRole(userEntity, role);
  }

  private void removeUserRole(UserEntity userEntity, Role role) {
    UserRoleEntity roleEntity =
            userEntity.checkOwnRoleAndReturn(role)
            .orElseThrow(
                    () -> new BadRequestException("The user does not have a role!")
            );
    if (userEntity.getUserRoles().size() < 2)
      throw new BadRequestException("The user must have at least one role!");
    log.info(roleEntity.getRole() + "  " + roleEntity.getId());
    deleteRoleAndUpdateUser(userEntity, roleEntity);
    log.info("after delete");
  }

  private void deleteRoleAndUpdateUser(UserEntity user, UserRoleEntity roleEntity) {
    userRoleRepository.delete(roleEntity);
    List<UserRoleEntity> newUserRoles = user
            .getUserRoles()
            .stream()
            .filter(e -> !e.equals(roleEntity))
            .collect(Collectors.toList());
    user.setUserRoles(newUserRoles);
    userRepository.save(user);
  }
}
