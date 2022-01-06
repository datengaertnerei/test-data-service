package com.datengaertnerei.test.dataservice.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("import")
public class DataImportTest {

	private static final String DATA_OSM_SMALL_PBF = "data/osm-small.pbf";
	@Autowired
	private PostalAddressRepository repository;

	@Test
	void contextLoads() {
		assertThat(repository).isNotNull();
	}

	@Test
	void shouldImport() {
		assertThat(OsmPbfAddressImportUtil.importAddresses(DATA_OSM_SMALL_PBF, repository)).isTrue();
	}

	@Test
	void shouldFailOnMissingFile() {
		assertThat(OsmPbfAddressImportUtil.importAddresses("will-not-exist", repository)).isFalse();
	}

	public static synchronized void ensureDataAvailability(PostalAddressRepository repository) {
		if (repository.count() == 0) {
			OsmPbfAddressImportUtil.importAddresses(DATA_OSM_SMALL_PBF, repository);
		}
	}

}
