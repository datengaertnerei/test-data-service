package com.datengaertnerei.test.dataservice.security;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for user entities in JPA
 *
 */
public interface UserRepository extends JpaRepository<TestDataUser, String> {

}
