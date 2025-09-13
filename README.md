# 농산물 정보 제공 백엔드 (Agriculture Contest)

## 1. 프로젝트 개요 (Overview)

- **프로젝트 이름**: Agriculture Contest Backend
- **한 줄 요약**: 농산물 정보 조회, 관리, AI 기반 챗봇 및 외부 데이터 연동 기능을 제공하는 Spring Boot 기반 백엔드 API 서버입니다.
- **핵심 가치**: 농산물 데이터를 체계적으로 관리하고, 외부 API와 연동하여 풍부한 정보를 제공하며, AI를 통해 사용자 맞춤형 서비스를 제공하는 것을 목표로 합니다. GitHub Actions와 Docker를 통해 빌드, 테스트, 배포 자동화를 구축하여 안정적이고 효율적인 개발 및 운영 환경을 제공합니다.

## 2. 주요 기능 (Features)

### 2.1. 농작물 정보 관리 (Crop API)
- 농작물에 대한 CRUD(생성, 조회, 수정, 삭제) 기능을 제공합니다.
- **Endpoint**: `/crop`
- **API 예시**:
  - `GET /crop/all`: 모든 농작물 정보 조회
  - `GET /crop/{id}`: 특정 ID의 농작물 정보 조회
  - `POST /crop`: 새 농작물 정보 생성

### 2.2. 네이버 백과사전 연동 검색
- 네이버 API와 연동하여 농작물에 대한 백과사전 정보를 실시간으로 검색하고 결과를 반환합니다.
- **Endpoint**: `GET /search?query={keyword}`

### 2.3. AWS S3 기반 파일 관리
- 이미지 등의 파일을 AWS S3에 업로드하고, Pre-signed URL을 발급하여 일정 시간 동안만 유효한 보안 링크를 제공합니다.
- **Endpoint**: `/s3`

### 2.4. AI 챗봇 (OpenAI 연동)
- `Spring AI` 라이브러리를 통해 OpenAI의 GPT 모델과 연동하여 사용자 질문에 답변하는 챗봇 기능을 제공합니다.
- `WeeklyChatTask` (`@Scheduled(cron = "0 0 12 * * MON")`)를 통해 매주 월요일 12시에 농업 관련 뉴스를 자동으로 요약합니다.

### 2.5. 정기적인 데이터 수집 (Scheduled Tasks)
- `@Scheduled` 어노테이션을 사용하여 매일 자정(`0 0 0 * * *`) 또는 매주 월요일 자정(`0 0 * * * MON`)에 특정 작업을 자동으로 수행합니다.
- `GrainV5Fetcher`를 통해 외부 농산물 데이터를 주기적으로 가져와 데이터베이스를 업데이트하는 기능의 기반을 마련했습니다.

## 3. 설치 및 실행 방법 (Installation & Usage)

### 3.1. 최소 요구사항
- **Java**: `17` (OpenJDK 17 Temurin)
- **Gradle**: `8.x` 이상
- **Database**: `MySQL`
- **Container**: `Docker`

### 3.2. 환경 변수 설정
- 프로젝트 실행을 위해 아래의 환경 변수 설정이 필요합니다. CI/CD 파이프라인에서는 GitHub Secrets를 통해, 로컬이나 직접 배포 시에는 `.env` 파일 또는 환경 변수로 주입해야 합니다.

```dotenv
# --- Database ---
DB_HOST=jdbc:mysql://localhost:3306
DB_NAME=your_db_name
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DB_CLASSNAME=com.mysql.cj.jdbc.Driver

# --- OpenAI API ---
OPENAI_API_KEY=your_openai_api_key

# --- AWS S3 ---
AWS_ACCESSKEY=your_aws_access_key
AWS_SECRETKEY=your_aws_secret_key
AWS_BUCKET_NAME=your_s3_bucket_name

# --- Naver API ---
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret

# --- CORS (쉼표로 구분) ---
ALLOWED_ORIGINS=http://localhost:3000,https://your-frontend.com
```

### 3.3. 빌드 및 실행

#### 1. 로컬에서 직접 실행
```bash
# 1. Gradle Wrapper에 실행 권한 부여 (최초 1회)
chmod +x ./gradlew

# 2. 프로젝트 빌드 (JAR 파일 생성)
./gradlew build

# 3. JAR 파일 실행 (환경변수 주입 필요)
java -jar build/libs/agriculture_contest-0.0.1-SNAPSHOT.jar
```

#### 2. Docker를 이용한 실행
`Dockerfile`은 Multi-stage build를 사용하여 경량화된 최종 이미지를 생성합니다.

```bash
# 1. Docker 이미지 빌드
docker build -t agriculture-backend:latest .

# 2. Docker 컨테이너 실행 (.env 파일 사용)
docker run -d --name agriculture-app -p 8080:8080 \
  --env-file ./.env \
  agriculture-backend:latest
```

### 3.4. 테스트
- 프로젝트의 유닛 테스트 및 통합 테스트를 실행합니다. CI 파이프라인의 첫 단계에서 자동으로 수행됩니다.
```bash
./gradlew test
```

## 4. 아키텍처 & 기술 스택 (Architecture & Tech Stack)

### 4.1. 주요 기술 스택
- **언어**: `Java 17`
- **프레임워크**: `Spring Boot 3.4.7`
  - `Spring Web` & `WebFlux`: 동기/비동기 처리를 위한 웹 기술
  - `Spring Data JPA`: 데이터베이스 연동
  - `Spring AI`: OpenAI 연동을 위한 추상화 라이브러리
  - `Spring Cloud OpenFeign`: 선언적 REST 클라이언트
  - `Spring Boot Actuator`: 애플리케이션 모니터링 (Health Check 등)
- **데이터베이스**: `MySQL`
- **빌드 도구**: `Gradle`
- **배포**: `Docker`

### 4.2. 외부 연동 서비스
- **AWS S3**: 파일 스토리지
- **OpenAI API**: AI 챗봇 기능
- **Naver Search API**: 백과사전 검색

### 4.3. CI/CD (GitHub Actions)
- `main` 브랜치에 `push` 또는 `pull_request` 시 워크플로우가 실행됩니다. (`.github/workflows/ci-cd.yml`)
- **파이프라인 순서**:
  1.  **Test**: `./gradlew test`를 실행하여 코드의 정합성을 검증합니다.
  2.  **Build & Push**: 테스트 통과 시, Gradle로 프로젝트를 빌드하고 Docker 이미지를 생성하여 Docker Hub에 `latest`와 `commit-hash` 태그로 푸시합니다.
  3.  **Deploy**: EC2 서버에 SSH로 접속하여 최신 Docker 이미지를 pull 받고, 기존 컨테이너를 교체하는 방식으로 무중단에 가까운 배포를 수행합니다.

### 4.4. 시스템 구조
- **Layered Architecture**:
  - **Controller**: API 엔드포인트 정의 및 HTTP 요청/응답 처리
  - **Service**: 핵심 비즈니스 로직 수행
  - **Repository**: `Spring Data JPA`를 이용한 데이터베이스 영속성 처리
  - **External**: `Feign Client`, `S3 Client` 등을 통해 외부 서비스와 통신
- **Configuration**: `application.yaml`과 `.env` 파일을 통해 설정을 외부로 분리하여 환경별 유연성을 확보하고, `CorsFilterConfiguration`을 통해 Cross-Origin 정책을 관리합니다.
