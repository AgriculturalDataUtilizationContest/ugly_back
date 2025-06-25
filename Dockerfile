# ---------- 1단계: 빌드 ----------
FROM openjdk:17-slim AS builder
WORKDIR /app

# 소스 복사
COPY . .

# ☝️ gradlew가 쓸 기본 유틸들 설치 (xargs = findutils)
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        git curl unzip findutils && \
    rm -rf /var/lib/apt/lists/*

# gradlew 실행권한 (윈도우에서 복사하면 빠질 수 있음)
RUN chmod +x ./gradlew

# JAR 빌드 (테스트 생략, 데몬 끔 → 메모리 절약)
RUN ./gradlew build -x test --no-daemon

# ---------- 2단계: 경량 런타임 ----------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
