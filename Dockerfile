FROM openjdk:19-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Ensure packages are up to date 
RUN apk update && apk upgrade --no-self-upgrade --available

# Import OSM dump without blowing up image size
RUN export OSM_IMPORT_FILE=http://ftp5.gwdg.de/pub/misc/openstreetmap/download.geofabrik.de/germany-latest.osm.pbf && export OSM_IMPORT_ONLY=YES && java -jar app.jar  

# Use Port environment variable to control listener
ENTRYPOINT export LISTEN="${PORT:=8080}" && java -Xms48M -Xmx48M -XX:+UseCompressedOops -jar app.jar --server.port=$LISTEN 