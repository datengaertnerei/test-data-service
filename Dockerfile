FROM eclipse-temurin:21-alpine

# create non-root user for security reasons
RUN addgroup -S testdata && adduser -S testdata -G testdata

RUN mkdir /opt/app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/app/app.jar

# Ensure packages are up to date 
RUN apk update && apk upgrade --no-self-upgrade --available

# Import OSM dump without blowing up image size
RUN export OSM_IMPORT_FILE=http://ftp5.gwdg.de/pub/misc/openstreetmap/download.geofabrik.de/germany-latest.osm.pbf && export OSM_IMPORT_ONLY=YES && cd /opt/app && java -jar app.jar  

#switch to non-root
USER testdata

# Use Port environment variable to control listener
ENTRYPOINT export LISTEN="${PORT:=8080}" && cd /opt/app && java -Xms48M -Xmx48M -XX:+UseCompressedOops -jar app.jar --server.port=$LISTEN 