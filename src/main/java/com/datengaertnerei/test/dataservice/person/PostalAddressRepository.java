/*MIT License

Copyright (c) 2020 Jens Dibbern

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.datengaertnerei.test.dataservice.person;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * JPA Repo for postal address entities
 *
 */
public interface PostalAddressRepository extends JpaRepository<PostalAddress, Long> {

	/**
	 * @param addressLocality addressLocality
	 * @return list of postalAddress objects
	 */
	List<PostalAddress> findByAddressLocalityIgnoreCase(String addressLocality);

	/**
	 * @param postalCode postalCode
	 * @return list of postalAddress objects
	 */
	List<PostalAddress> findByPostalCodeStartsWith(String postalCode);

	/**
	 * @param likeQuery postalCode like query
	 * @return list of postalAddress objects
	 */
	@Query(value = "SELECT p FROM PostalAddress p WHERE postal_code LIKE ?1")
	List<PostalAddress> findByPostalCodeLike(String likeQuery);
	
	/**
	 * @return first id
	 */
	@Query(value = "SELECT min(id) FROM PostalAddress")
	public Long min();

	/**
	 * @return last id
	 */
	@Query(value = "SELECT max(id) FROM PostalAddress")
	public Long max();
}
