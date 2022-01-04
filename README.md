# test data service

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/484d467e7e4540c5b8b7fbce78538bfc)](https://www.codacy.com/manual/datengaertnerei/test-data-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=datengaertnerei/test-data-service&amp;utm_campaign=Badge_Grade) 
[![codecov](https://codecov.io/gh/datengaertnerei/test-data-service/branch/develop/graph/badge.svg?token=3V6AFYMMQA)](https://codecov.io/gh/datengaertnerei/test-data-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=datengaertnerei_test-data-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=datengaertnerei_test-data-service)
[![Maven Build](https://github.com/datengaertnerei/test-data-service/workflows/maven-build/badge.svg)](https://github.com/datengaertnerei/test-data-service)
[![Docker Image Size (tag)](https://img.shields.io/docker/image-size/datengaertner/test-data-service/latest)](https://hub.docker.com/repository/docker/datengaertner/test-data-service)
[![License Badge](https://img.shields.io/github/license/datengaertnerei/test-data-service.svg)](https://mit-license.org/)

A Spring Boot REST service to generate test data for german persons incl. address, phone, mobile and current account IBAN to provide GDPR compliant test data. It is [adaptable](Adapt.md) to other countries based on OpenStreetMap and other public data sources.

## Another test data generator? Really?

At first I needed valid postal addresses within Germany. I did not find anything usable to meet that requirement. So I chose Open Street Map as a source of valid postal addresses. And as you usually do not want to include a dozen different generators in your test, I kept adding things I needed in my projects.

## We are just using a copy of our production database

You should meet a german data protection official. And most of the time this contains either too much data and/or not the data you need for your test.

## How to set it up

It is a Spring Boot application. The service will import [Open Streetmap PBF dumps](https://download.geofabrik.de/europe/germany.html) by using
```
export OSM_IMPORT_FILE=germany-latest.osm.pbf
export OSM_IMPORT_ONLY=YES
java -jar data-service.jar
```

The easy way to setup the test data service is to use Docker. Just run the latest [docker image](https://hub.docker.com/repository/docker/datengaertner/test-data-service)

```
docker run --name test-data -p 80:8080 datengaertner/test-data-service
```

There are Docker files included to create your own images. 

## How to use it

After startup you can navigate to [https://localhost:8443/](https://localhost:8443/) in your browser. It will ask for Basic Authentication (admin and the generated password) and then show a random generated person. There is a link on that page to the included Swagger UI.

![User Interface](https://user-images.githubusercontent.com/44938643/94337950-90829e00-ffee-11ea-9669-d7dc19e53b75.png)

With Swagger UI as usual you can test API calls and get JSON samples for your own clients.

![Swagger UI](https://user-images.githubusercontent.com/44938643/94337964-be67e280-ffee-11ea-951b-576f16af2661.png)

You can access the [API at SwaggerHub](https://app.swaggerhub.com/apis/datengaertnerei1/datengartnerei-test_data_service_api/) and there is a simple Go client available as a [Gist](https://gist.github.com/datengaertnerei/680a1244439d6dfee9a51dd35430cf5d).

## Your service does not provide \<your data type here>

There are many other [sources of test data](Testdata.md).

## Has it been tested?
Well, yes, obviously

And it is an example how to use different test automation tools for Spring Boot.

## But I want test data for Mexico
[Follow me!](Adapt.md)

## Disclaimer

DO NOT USE generated data, especially IBAN or credit card number, anywhere else than in your confined test environment. Someone might prosecute you for fraud.
