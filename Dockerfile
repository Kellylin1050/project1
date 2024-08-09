# 使用OpenJDK 17映像檔作為基礎映像檔
FROM openjdk:17

# 設定工作目錄為/app
WORKDIR /app

# 將本機目錄下的Spring Boot應用程式可執行JAR複製到容器中
COPY target/project1-0.0.1-SNAPSHOT.jar app.jar

# 定義ENTRYPOINT指令以運行Spring Boot應用程式
RUN ["java", "-jar", "app.jar"]