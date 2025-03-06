package com.eloiza.tax_calculator.repositories;

import com.eloiza.tax_calculator.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
