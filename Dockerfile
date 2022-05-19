FROM azul/zulu-openjdk-debian:8  AS builder

RUN mkdir /fineract

COPY . /fineract/

WORKDIR /fineract

RUN ./mvn clean package

FROM azul/zulu-openjdk-debian:8 as runner

RUN mkdir -p /app/

COPY --from=builder /fineract/openbanking-adapter/target/openbanking-adapter-1.0.0-SNAPSHOT.jar /app/operations-app.jar

WORKDIR /

EXPOSE 8080 5000 62020 62021

CMD java -jar /app/operations-app.jar