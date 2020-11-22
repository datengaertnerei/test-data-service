FROM openjdk:14-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY data/testdata.mv.db data/testdata.mv.db  
ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE