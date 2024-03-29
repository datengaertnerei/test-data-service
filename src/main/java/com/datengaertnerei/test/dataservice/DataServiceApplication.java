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

package com.datengaertnerei.test.dataservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Spring Boot main application for test data service
 *
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class DataServiceApplication {
	private static final String IMPORT_ONLY_YES = "YES";
	private static Logger log = LoggerFactory.getLogger(DataServiceApplication.class);

	@Value("${application.version}")
	private String apiVersion;
	
	/**
	 * @param args cli arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DataServiceApplication.class, args);
	}

	/**
	 * @param ctx Spring Boot app contect
	 * @return Spring Boot runner object
	 */
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			String[] beanNames = ctx.getBeanDefinitionNames();
			log.info("Started with bean count of {}", beanNames.length);

			String importOnly = System.getenv("OSM_IMPORT_ONLY");
			if (null != importOnly && IMPORT_ONLY_YES.equalsIgnoreCase(importOnly)) {
				log.info("Import only mode - exiting");
				((ConfigurableApplicationContext) ctx).close();
			}

		};
	}

	/**
	 * @return OpenAPI doc bean
	 */
	@Bean
	public OpenAPI springOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Datengärtnerei Test Data Service API")
						.description("We provide ad hoc generated test data for fictitious german persons.")
						.version(apiVersion)
						.license(new License().name("MIT License")
								.url("https://github.com/datengaertnerei/test-data-service/blob/master/LICENSE")))
				.externalDocs(
						new ExternalDocumentation().description("Datengärtnerei").url("https://datengaertnerei.com/"));
	}
}
