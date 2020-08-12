/**
 * 
 */
package com.datengaertnerei.test.dataservice.person;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jensd
 *
 */
public interface PostalAddressRepository extends JpaRepository<PostalAddress, Integer> {
}
