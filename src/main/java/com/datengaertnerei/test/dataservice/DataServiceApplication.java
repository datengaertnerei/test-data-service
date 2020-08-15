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

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DataServiceApplication {
	private static Logger log = LoggerFactory.getLogger(DataServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DataServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			String[] beanNames = ctx.getBeanDefinitionNames();
			log.info("Started with bean count of " + beanNames.length);

		};
	}

	@Bean
	public Docket testDataApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("test-data-service-api").apiInfo(apiInfo()).select()
				.paths(paths()).build();
	}

	private Predicate<String> paths() {
		return regex(".*/api/v1.*");
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Datengärtnerei Test Data Service API")
				.description("We provide ad hoc generated test data for fictitious german persons.")
				.contact(new Contact("Datengärtnerei", "https://datengaertnerei.com", "")).license("MIT License")
				.licenseUrl("https://github.com/datengaertnerei/test-data-service/blob/master/LICENSE").build();
	}
}
