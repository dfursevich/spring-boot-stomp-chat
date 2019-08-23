FROM openjdk:10

RUN mkdir -p /opt/chat
COPY target/chat.jar /opt/chat
WORKDIR /opt/chat
EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "java -jar chat.jar"]