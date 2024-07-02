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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.io.input.BrokenInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReader;

/**
 * OpenStreetMap address import tool.
 *
 * @author Jens Dibbern
 */
public class OsmPbfAddressImportUtil {

	private static Log log = LogFactory.getLog(OsmPbfAddressImportUtil.class);

	/**
	 * Utility class should not be instantiated
	 */
	private OsmPbfAddressImportUtil() {
		super();
	}

	/**
	 * Starts OSM dump parser and exports all addresses within defined country.
	 *
	 * @param inputUrl   the file to import
	 * @param repository reference to the JPA repository
	 * @return success
	 * @throws MalformedURLException import URL not valid
	 */
	public static boolean importAddresses(String inputUrl, PostalAddressRepository repository) throws MalformedURLException {

		URL url = new URL(inputUrl);
		
		Supplier<InputStream> supplier = () -> {
			try {
				return url.openStream();
			} catch (IOException e) {
				log.error("could not open OSM stream", e);
				return new BrokenInputStream();
			}
		};
		PbfReader reader = new PbfReader(supplier, 1);
		Sink sinkImplementation = new AddressSink(repository);
		reader.setSink(sinkImplementation);

		try {
			reader.run();
		} catch (Exception e) {
			log.error("OSM import run failed", e);
			return false;
		}
		return true;

	}
}

class AddressSink implements Sink {
	private static Log log = LogFactory.getLog(AddressSink.class);

	private static final String COUNTRY = "DE";
	private static final String ADDR_POSTCODE = "addr:postcode";
	private static final String ADDR_HOUSENUMBER = "addr:housenumber";
	private static final String ADDR_STREET = "addr:street";
	private static final String ADDR_CITY = "addr:city";
	private static final String ADDR_COUNTRY = "addr:country";

	private PostalAddressRepository repository;
	private int importCounter = 0;
	private HashSet<String> postCodes = new HashSet<>(); 

	/**
	 * @param repository
	 */
	public AddressSink(PostalAddressRepository repository) {
		super();
		this.repository = repository;
	}

	/**
	 *
	 */
	public void process(EntityContainer entityContainer) {

		try {
			Entity entity = entityContainer.getEntity();
			if (entity instanceof Node) {
				processNode(entity);
			}
		} catch (Exception e) {
			log.error("OSM import error", e);
		}
	}

	private void processNode(Entity entity) {
		Node node = (Node) entity;
		extractPostalAddress(node);
		// log progress
		if (importCounter++ % 1000000 == 0) {
			log.info("processing " + importCounter);
		}
	}

	private void extractPostalAddress(Node node) {
		Collection<Tag> tags = node.getTags();
		String country = null;
		String city = null;
		String street = null;
		String housenumber = null;
		String postcode = null;
		int tagCount = 0;
		for (Tag tag : tags) {
			switch (tag.getKey()) {
			case ADDR_COUNTRY:
				country = tag.getValue();
				tagCount++;
				break;
			case ADDR_CITY:
				city = tag.getValue();
				tagCount++;
				break;
			case ADDR_STREET:
				street = tag.getValue();
				tagCount++;
				break;
			case ADDR_HOUSENUMBER:
				housenumber = tag.getValue();
				tagCount++;
				break;
			case ADDR_POSTCODE:
				postcode = tag.getValue();
				tagCount++;
				break;

			default: // just skip to the next tag
			}
		}
		if (tagCount > 4 && country != null && country.equals(COUNTRY) && !postCodes.contains(postcode)) {

			postCodes.add(postcode);
			PostalAddress pa = new PostalAddress(country, city, postcode, street, housenumber);
			repository.saveAndFlush(pa);
		}
	}

	@Override
	public void initialize(Map<String, Object> metaData) {
		// just to comply with the interface
	}

	@Override
	public void complete() {
		// just to comply with the interface
	}

	@Override
	public void close() {
		// just to comply with the interface
	}
}