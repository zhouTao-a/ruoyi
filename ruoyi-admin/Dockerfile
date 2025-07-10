FROM openjdk:17

# 设置容器时区为 Asia/Shanghai
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

COPY admin.jar .

# 设置 JVM 启动时区
CMD ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "admin.jar"]
