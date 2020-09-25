package yte.intern.project.manageActivities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.manageActivities.entity.Users;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByTcKimlikNo(String tcKimlikNo);

    boolean existsByTcKimlikNo(String tcKimlikNo);

    @Transactional
    void deleteByTcKimlikNo(String tcKimlikNo);


}
