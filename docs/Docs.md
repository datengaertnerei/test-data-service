# test data service

## How to set it up

It is a Spring Boot application. The service will import [Open Streetmap PBF dumps](https://download.geofabrik.de/europe/germany.html) by using
```
export OSM_IMPORT_FILE=http://ftp5.gwdg.de/pub/misc/openstreetmap/download.geofabrik.de/germany-latest.osm.pbf
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

