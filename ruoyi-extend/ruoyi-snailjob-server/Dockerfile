FROM openjdk:17

# 设置容器时区为 Asia/Shanghai
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# 复制 jar 包
COPY snailjob.jar .

# 启动时设置 JVM 时区为 Asia/Shanghai，防止 Java 使用 UTC
CMD ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "snailjob.jar"]
