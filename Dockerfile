FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN apk update
RUN apk upgrade --no-self-upgrade --available

RUN apk --no-cache add curl
RUN curl http://ftp5.gwdg.de/pub/misc/openstreetmap/download.geofabrik.de/germany-latest.osm.pbf -o germany-latest.osm.pbf
RUN sh -c 'export OSM_IMPORT_FILE=germany-latest.osm.pbf && export OSM_IMPORT_ONLY=YES && java -jar app.jar' 
RUN rm -f germany-latest.osm.pbf

ENTRYPOINT java -jar app.jar