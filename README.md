# Spring Boot JWT Auth Server

팀 스파르타 바로인턴 백엔드 직무 과제,
간단한 **JWT 기반 인증/인가 서비스**입니다.  
AWS EC2에 배포되어 있으며, 사용자 회원가입·로그인·권한 부여 기능을 제공합니다.

---

## 📌 서버 정보

- **Base URL**: `http://3.36.196.106:8080`
- **Swagger UI**: [http://3.36.196.106:8080/swagger-ui/index.html](http://3.36.196.106:8080/swagger-ui/index.html)

---

## 🚀 주요 기능

1. **회원가입**
   - `POST /signup`
   - 요청 시 사용자 계정을 생성합니다. (기본 Role: USER)

2. **로그인**
   - `POST /login`
   - JWT Access Token을 발급합니다.  
   - 모든 보호된 API 호출 시 헤더에 토큰 포함 필요:
     ```
     Authorization: Bearer <JWT_TOKEN>
     ```

3. **관리자 회원 가입**
   - `POST /admin/signup`
   - Admin 코드를 입력받아 관리자로 회원 가입합니다. (AdminCode : "123456")

4. **관리자 권한 부여**
   - `PATCH /admin/users/{userId}/roles`
   - ADMIN Role이 있는 사용자만 호출 가능.

---

## 🔑 기본 계정

- **관리자(Admin)**
   - username: admin
   - password: admin123


---

## 📖 문서

- Swagger 문서에서 API 상세를 확인할 수 있습니다:  
👉 [Swagger UI 바로가기](http://3.36.196.106:8080/swagger-ui/index.html)
