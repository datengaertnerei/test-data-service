FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Ensure packages are up to date and install curl
RUN apk update && apk upgrade --no-self-upgrade --available && apk --no-cache add curl

# Import OSM dump without blowing up image size
RUN sh -c 'curl http://ftp5.gwdg.de/pub/misc/openstreetmap/download.geofabrik.de/germany-latest.osm.pbf -o germany-latest.osm.pbf && export OSM_IMPORT_FILE=germany-latest.osm.pbf && export OSM_IMPORT_ONLY=YES && java -jar app.jar && rm -f germany-latest.osm.pbf' 

# Use Port environment variable to control listener
ENTRYPOINT export LISTEN="${PORT:=8080}" && java -jar app.jar --server.port=$LISTEN -XX:+UseSerialGC -XXfullCompaction -XX:+UseCompressedOops -XX:MaxRAM=100m