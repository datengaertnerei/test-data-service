package com.datengaertnerei.test.dataservice.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.nio.file.Path;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class to test OSM dump import feature
 *
 */
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
		try {
			assertThat(OsmPbfAddressImportUtil.importAddresses(Path.of(DATA_OSM_SMALL_PBF).toUri().toURL().toString(),
					repository)).isTrue();
		} catch (MalformedURLException e) {
			fail(e);
		}
	}

	@Test
	void shouldFailOnMissingFile() {
		assertThrows(MalformedURLException.class, () -> {
			OsmPbfAddressImportUtil.importAddresses("will-not-exist", repository);
		});
	}

	/**
	 * Check for available records in repository and import test file if empty
	 * 
	 * @param repository the address repository
	 */
	public static synchronized void ensureDataAvailability(PostalAddressRepository repository) {
		if (repository.count() == 0) {
			try {
				OsmPbfAddressImportUtil.importAddresses(Path.of(DATA_OSM_SMALL_PBF).toUri().toURL().toString(),
						repository);
			} catch (MalformedURLException e) {
				fail(e);
			}
		}
	}

}
