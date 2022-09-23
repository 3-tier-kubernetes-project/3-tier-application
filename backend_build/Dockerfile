FROM cptbluebear/maven-base:1.0 as maven-stage
WORKDIR /sourcecode
ADD ./sourcecode .
RUN mvn package -Dmaven.test.skip=true

FROM cptbluebear/jre-base:1.0 as jdk-stage
COPY --from=maven-stage /sourcecode/target/DockerBackendSample-0.0.1-SNAPSHOT.jar /server/server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/server/server.jar"]

