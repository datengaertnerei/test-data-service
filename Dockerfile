# use this for Heroku and specify your admin password here
FROM openjdk:14-alpine
ARG JAR_FILE=target/*.jar
ENV TD_ADMIN_PASSWD=<your_admin_password>
COPY ${JAR_FILE} app.jar
COPY data/testdata.mv.db data/testdata.mv.db  
COPY external.properties external.properties  
ENTRYPOINT ["java","-jar","/app.jar", "--spring.config.location=external.properties"]