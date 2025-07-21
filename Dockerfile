FROM eclipse-temurin:17-jre

FROM openjdk:17-slim

WORKDIR /app

# 复制对应环境的配置文件
COPY /src/main/resources/application.yml /config/application.yml

# 复制应用程序jar包
COPY target/*.jar /app.jar

# 设置环境变量
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_CONFIG_LOCATION=/config/application.yml
ENV SERVER_PORT=8001
ENV SERVER_ADDRESS=0.0.0.0
ENV SPRING_PROFILES_ACTIVE=prod
ENV REDIS_HOST=8.137.95.47
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=123456
ENV REDIS_DATABASE=2
ENV CORS_ALLOWED_ORIGIN_PATTERNS=http://47.109.24.42:*,http://localhost:*,*://*:80,*://*:8011
ENV CORS_ALLOWED_CREDENTIALS=true

EXPOSE ${SERVER_PORT}



ENTRYPOINT ["java", "-jar", "/app.jar", "/config/application.yml"]
