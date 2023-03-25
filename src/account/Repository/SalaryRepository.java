package account.Repository;

import account.Entities.SalaryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryEntity,Long> {
  Optional<SalaryEntity> findTopDistinctByPeriodAndEmailIgnoreCase(String period, String userEmail);

  List<SalaryEntity> findAllByEmailIgnoreCase(String email, Sort sort);

}
