FROM amazoncorretto:11

ENV JAVA_OPTS="-Xmx1g"

ADD ./target/sns-springboot-1.0.0.jar /app.jar
ADD ./entrypoint /entrypoint

RUN chmod +x /entrypoint

EXPOSE 8201

ENTRYPOINT [ "/entrypoint" ]
