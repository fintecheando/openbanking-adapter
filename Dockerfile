FROM azul/zulu-openjdk-debian:11 as runner

RUN mkdir -p /app/libs

COPY /openbanking-adapter/target/openbanking-adapter-1.0.0-SNAPSHOT.jar /app/operations-app.jar

WORKDIR /

EXPOSE 8080

CMD java --add-modules java.xml.ws -jar /app/operations-app.jar