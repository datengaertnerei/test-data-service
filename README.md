# test data service

[![Website Monitor](https://img.shields.io/website?up_message=up&url=https%3A%2F%2Fdatengaertnerei.github.io%2Ftest-data-service%2F)](https://datengaertnerei.github.io/test-data-service/) 
[![codecov](https://codecov.io/gh/datengaertnerei/test-data-service/branch/develop/graph/badge.svg?token=3V6AFYMMQA)](https://codecov.io/gh/datengaertnerei/test-data-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=datengaertnerei_test-data-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=datengaertnerei_test-data-service)
[![Maven Build](https://github.com/datengaertnerei/test-data-service/workflows/maven-build/badge.svg)](https://github.com/datengaertnerei/test-data-service)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/6044/badge)](https://bestpractices.coreinfrastructure.org/projects/6044)
[![License Badge](https://img.shields.io/github/license/datengaertnerei/test-data-service.svg)](https://mit-license.org/)

A Spring Boot REST service to generate test data for german persons incl. address, phone, mobile and current account IBAN to provide GDPR compliant test data. It is [adaptable](Adapt.md) to other countries based on OpenStreetMap and other public data sources.

## Another test data generator? Really?

At first I needed valid postal addresses within Germany. I did not find anything usable to meet that requirement. So I chose Open Street Map as a source of valid postal addresses. And as you usually do not want to include a dozen different generators in your test, I kept adding things I needed in my projects.

## We are just using a copy of our production database

You should meet a german data protection official. And most of the time this contains either too much data and/or not the data you need for your test.

## How to use it

The easy way to setup the test data service is to use Docker. Just run the latest [docker image](https://hub.docker.com/repository/docker/datengaertner/test-data-service)

```
docker run --name test-data -p 80:8080 datengaertner/test-data-service
```

After startup you can navigate to [http://localhost/](http://localhost/) in your browser. It will show a random generated person. There is a link on that page to the included Swagger UI.

For more details please proceed to the [docs](docs/Docs.md) or the [project pages with CI/CD info](https://datengaertnerei.github.io/test-data-service/). If you encounter any problems or identify an error please submit an issue in GitHub.

## Disclaimer

DO NOT USE generated data, especially IBAN or credit card number, anywhere else than in your confined test environment. Someone might prosecute you for fraud.
