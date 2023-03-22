package account.Repository;

import account.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findTopDistinctByEmailIgnoreCase(String email);

  // 更改语句
  @Modifying
  @Transactional
  @Query(
          value = "UPDATE user SET password = ?1 WHERE UPPER(email) = UPPER(?2)",
          nativeQuery = true
  )
  int updateUserPwdByEmail(String password, String email);
}
