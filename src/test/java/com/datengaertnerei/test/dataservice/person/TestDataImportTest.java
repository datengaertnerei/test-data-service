package com.datengaertnerei.test.dataservice.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.datengaertnerei.test.dataservice.DataServiceApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("import")
class TestDataServiceApiTests {

	@Autowired
	private PostalAddressRepository repository;

	@Test
	void contextLoads() {
		assertThat(repository).isNotNull();
	}

	/**
	 * Consecutive calls should create different (random) results for
	 * Person/PostalAddress
	 */
	@Test
	void shouldImport() {
		assertThat(OsmPbfAddressImportUtil.importAddresses("data/osm-small.pbf", repository)).isTrue();
	}

}
