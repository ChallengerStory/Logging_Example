# ─────────────────────────────────────────────────────────────────────────────
# 1) Build 단계 (optional)
#    만약 Docker 안에서 Gradle/Maven 빌드를 같이 수행하고 싶으면 이 섹션을 사용합니다.
#    그렇지 않고 이미 로컬에서 JAR이 만들어져 있다면 2) Runtime 단계만 사용하세요.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk AS build

# (선택) 소스코드를 복사하여 빌드
WORKDIR /workspace

# Gradle 프로젝트일 때:
COPY build.gradle settings.gradle gradlew /workspace/
COPY gradle /workspace/gradle
RUN chmod +x gradlew

# 소스만 먼저 복사하여 의존성만 다운받기 (레이어 캐싱 활용)
COPY src /workspace/src
RUN ./gradlew clean bootJar

# ─────────────────────────────────────────────────────────────────────────────
# 2) Runtime 단계
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-focal

# JVM 옵션 (필요에 따라 조정)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 컨테이너 내 앱 디렉터리 생성
WORKDIR /app

# build 단계에서 생성된 JAR을 복사
COPY --from=build /workspace/build/libs/*.jar app.jar

# (만약 로컬에서 이미 JAR을 만들어 두었다면, 다음 2줄만 필요)
# FROM eclipse-temurin:17-jre-focal
# COPY target/myapp.jar /app/app.jar

# Spring Boot 기본 포트 노출
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]