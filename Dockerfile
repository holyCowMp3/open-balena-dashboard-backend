FROM java:8-jdk-alpine
COPY . /usr/app/
WORKDIR /usr/app
EXPOSE 6040
ARG BALENA_URL
ARG PORT
EXPOSE ${PORT}
RUN mv target/*.jar gateway.jar
CMD java -jar -Dbalena.api=$BALENA_URL \
              $EXTRA_ARG \
              gateway.jar