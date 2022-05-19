FROM azul/zulu-openjdk-debian:17 as runner

RUN mkdir -p /app/libs

COPY /target/backend-0.0.1-SNAPSHOT.jar /app/operations-app.jar

WORKDIR /

EXPOSE 8080

CMD java -jar /app/operations-app.jar