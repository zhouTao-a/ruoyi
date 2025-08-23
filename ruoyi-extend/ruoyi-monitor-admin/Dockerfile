FROM openjdk:17

# 设置时区为 Asia/Shanghai
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

COPY monitor.jar .

# 设置 JVM 时区，防止 Java 使用 UTC
CMD ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "monitor.jar"]
