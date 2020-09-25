package yte.intern.project.manageActivities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.manageActivities.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);
}
