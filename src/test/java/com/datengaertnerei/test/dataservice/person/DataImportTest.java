package com.datengaertnerei.test.dataservice.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("import")
public class DataImportTest {

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

	public static synchronized void ensureDataAvailability(PostalAddressRepository repository) {
		if (repository.count() == 0) {
			OsmPbfAddressImportUtil.importAddresses("data/osm-small.pbf", repository);
		}
	}

}
