export TAG=0.7
docker build -t datengaertner/test-data-service:$TAG .
docker push datengaertner/test-data-service:$TAG
docker push datengaertner/test-data-service:latest
#heroku container:push web -a test-data-generator
#heroku container:release web -a test-data-generator

