FROM gradle:7.5.0-jdk17
WORKDIR /opt/app
COPY ./build/libs/RealtyHubBackend-1.0-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar RealtyHubBackend-1.0-SNAPSHOT.jar"]