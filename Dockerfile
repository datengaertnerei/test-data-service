FROM openjdk:22-slim

# create non-root user for security reasons
RUN addgroup testdata && adduser testdata --ingroup testdata

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Ensure packages are up to date 
RUN apt-get update && apt-get upgrade -y && apt-get autoremove && apt-get autoclean

# Import OSM dump without blowing up image size
RUN export OSM_IMPORT_FILE=http://ftp5.gwdg.de/pub/misc/openstreetmap/download.geofabrik.de/germany-latest.osm.pbf && export OSM_IMPORT_ONLY=YES && java -jar app.jar  

#switch to non-root
USER testdata

# Use Port environment variable to control listener
ENTRYPOINT export LISTEN="${PORT:=8080}" && java -Xms48M -Xmx48M -XX:+UseCompressedOops -jar app.jar --server.port=$LISTEN 