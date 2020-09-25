package yte.intern.project.manageActivities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.manageActivities.entity.Activity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity,Long> {

    Optional<Activity> findByTitle(String activityTitle);

    boolean existsByTitle(String activityTitle);

    List<Activity> findByStartDateAfter(LocalDate now);

    @Transactional
    void deleteByTitle(String activityTitle);
}
