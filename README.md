# SPRING PLUS

## 기술 스택

- Java 17
- Spring Boot
- Spring Data JPA
- QueryDSL
- Spring Security
- JWT
- H2 / MySQL
- JUnit5, MockMvc

---

## 주요 구현 내용
### 핵심 기능
- `@Transactional` 옵션 이해 및 오류 수정
- JWT 기반 인증 및 사용자 정보(`nickname`) 클레임 확장
- JPQL · QueryDSL을 활용한 동적 검색 구현
- 컨트롤러 테스트 및 예외 검증 정합성 확보
- AOP 기반 관리자 권한 변경 로깅

### 성능 & 구조 개선
- JPA Cascade 적용으로 연관 데이터 자동 처리
- fetch join / QueryDSL을 통한 N+1 문제 해결
- JPQL → QueryDSL 리팩토링

### 보안
- Spring Security 기반 Stateless JWT 인증 적용
- 역할(Role) 기반 접근 제어 유지

### 도전 과제
- QueryDSL 검색 API
  - 제목·담당자 닉네임 검색
  - 담당자 수·댓글 수 집계
  - 페이징 및 정렬
- 트랜잭션 심화
  - 로그 테이블 분리
  - 독립 트랜잭션으로 로그 상시 기록
