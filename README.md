# test data service

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/484d467e7e4540c5b8b7fbce78538bfc)](https://www.codacy.com/manual/datengaertnerei/test-data-service?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=datengaertnerei/test-data-service&amp;utm_campaign=Badge_Grade)

[![Maven Build](https://github.com/datengaertnerei/test-data-service/workflows/maven-build/badge.svg)](https://github.com/datengaertnerei/test-data-service)

A Spring Boot REST service to generate test data for german persons incl. address, phone, mobile and current account IBAN.

##Another test data generator? Really?##

At first I needed valid postal addresses within Germany. I did not find anything usable to meet that requirement. So I chose Open Street Map as a source of valid postal addresses. And as you usually do not want to include a dozen different generators in your test, I kept adding things I needed in my projects.

## We are just using a copy of our production database##

You should meet a german data protection official. And most of the time this contains either too much data and/or not the data you need for your test.

## How to use it##

It is a Spring Boot application. You can just start it and during the first start you will see the generated admin password in the log:

'''
using generated password for admin account: 
'''

You have to keep that in your password vault. It will not show again. Currently you can only reset it by removing it from the embedded H2 database manually.

After startup you can navigate to [https://localhost:8443/](https://localhost:8443/) in your browser. It will ask for Basic Authentication (admin and the generated password) and the show a random generated person. There is a link on that page to the included Swagger UI.