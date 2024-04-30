#FROM ubuntu:latest
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 프로젝트 빌드 결과로 생성된 jar 파일을 컨테이너에 복사
COPY build/libs/SpaghettiCodingClub-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/spring-boot-0.0.1-SNAPSHOT.jar"]