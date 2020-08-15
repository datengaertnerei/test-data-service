package com.datengaertnerei.test.dataservice.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TestDataUser, String> {

}
