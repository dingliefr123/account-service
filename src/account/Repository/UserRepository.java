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

  @Modifying
  @Transactional
  @Query(
          value = "UPDATE user SET password = ?1 WHERE UPPER(email) = UPPER(?2)",
          nativeQuery = true
  )
  int updateUserPwdByEmail(String password, String email);

  @Modifying
  @Transactional
  @Query(
          value = "UPDATE user SET locked = true WHERE UPPER(email) = UPPER(?1)",
          nativeQuery = true
  )
  int lockUserByEmail(String email);

  @Modifying
  @Transactional
  @Query(
          value = "UPDATE user SET wrong_input_cnt = 0, locked = false  WHERE UPPER(email) = UPPER(?1)",
          nativeQuery = true
  )
  int clearWrongCntByAndUlockEmail(String email);

  @Modifying
  @Transactional
  @Query(
          value = "UPDATE user SET wrong_input_cnt = wrong_input_cnt + 1 WHERE UPPER(email) = UPPER(?1)",
          nativeQuery = true
  )
  int updateWrongCntByEmail(String email);
}
