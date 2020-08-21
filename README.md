# test data service

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/484d467e7e4540c5b8b7fbce78538bfc)](https://www.codacy.com/manual/datengaertnerei/test-data-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=datengaertnerei/test-data-service&amp;utm_campaign=Badge_Grade) 
[![Maven Build](https://github.com/datengaertnerei/test-data-service/workflows/maven-build/badge.svg)](https://github.com/datengaertnerei/test-data-service)
[![Dependencies](https://img.shields.io/librariesio/github/datengaertnerei/test-data-service.svg)](https://libraries.io/github/datengaertnerei/test-data-service)
[![DepShield Badge](https://depshield.sonatype.org/badges/datengaertnerei/test-data-service/depshield.svg)](https://depshield.github.io)
[![License Badge](https://img.shields.io/github/license/datengaertnerei/test-data-service.svg)](https://mit-license.org/)

A Spring Boot REST service to generate test data for german persons incl. address, phone, mobile and current account IBAN.

## Another test data generator? Really? ##

At first I needed valid postal addresses within Germany. I did not find anything usable to meet that requirement. So I chose Open Street Map as a source of valid postal addresses. And as you usually do not want to include a dozen different generators in your test, I kept adding things I needed in my projects.

## We are just using a copy of our production database ##

You should meet a german data protection official. And most of the time this contains either too much data and/or not the data you need for your test.

## How to use it ##

It is a Spring Boot application. You can just start it and during the first start you will see the generated admin password in the log:

```
using generated password for admin account: <xxx>
```

You have to keep that in your password vault. It will not show again. Currently you can only reset it by removing it from the embedded H2 database manually.

After startup you can navigate to [https://localhost:8443/](https://localhost:8443/) in your browser. It will ask for Basic Authentication (admin and the generated password) and the show a random generated person. There is a link on that page to the included Swagger UI.

![image](https://user-images.githubusercontent.com/44938643/90776994-242fc300-e2fb-11ea-83c7-0fafdb2a70ed.png)

With Swagger UI as usual you can test API calls and get JSON samples for your own clients.

![image](https://user-images.githubusercontent.com/44938643/90777160-53463480-e2fb-11ea-9c05-d6c38ae576d0.png)

There is a simple Go client available as a [Gist](https://gist.github.com/datengaertnerei/680a1244439d6dfee9a51dd35430cf5d).

## Security setup ##

TLS and Basic Authentication are configured as default. TLS uses a self-signed certificate, that you should replace in your environment. It is configured by Spring Boot application properties, that you can provide externally at startup:

```
server.ssl.key-store-type=PKCS12
server.ssl.key-store=classpath:keystore/testdata.p12
server.ssl.key-store-password=O0mph!
server.ssl.key-alias=testdata
```

I am using Basic Authentication just to be able to protect the service infrastructure from abuse. There is no default admin password. It will be generated at first start (see above). 

## Your service does not provide \<your data type here> ##

There is always [Faker](https://github.com/DiUS/java-faker). I will not reproduce Faker. Maybe I will include it as a retirement task. But there are Faker forks for almost any programming language. Faker is ok as long as you do not need valid(!) data for certain purposes like postal addresses, IBANs for current accounts or credit card numbers. My fellow german software developers tend to strictly validate their input data.

## Disclaimer ##

DO NOT USE generated data, especially IBAN or credit card number, anywhere else than in your confined test environment. Someone might prosecute you for fraud.
