package com.eloiza.tax_calculator.repositories;

import com.eloiza.tax_calculator.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
