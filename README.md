# 🥐Paris Baguette 직원 관리 자동화 프로젝트 명세서

📌 사용자 중심 공지/이벤트/휴가 예약/신입 가이드 관리 시스템

- **Spring Boot 기반 웹 프로젝트**
- **관리자 + 사용자용 기능 분리 구성**
- **Thymeleaf + REST 스타일 일부 적용**

--------------------------------------------------------------

## 🛠 기술 스택

### ⚙️ Backend

- Java 17
- Spring Boot `3.4.1`
- MyBatis
- Oracle 11g
- Gradle

### 🗃 DB & API

- Oracle SQL
- MyBatis Mapper XML
- YouTube Data API
- CoolSMS API

### 🌐 Frontend

- Thymeleaf
- JavaScript (AJAX, 모달, 페이징, 무한스크롤)
- HTML5 + CSS3 (반응형 커스터마이징)

### 🧰 기타 도구

- IntelliJ IDEA
- SQL Developer
- GitHub

---

## 📦 프로젝트 주요 기능

---

<img width="320" height="320" alt="공지 메인" src="https://github.com/user-attachments/assets/afbd2f91-4e01-4637-a4e4-55f15584b945" />
<img width="320" height="320" alt="게시글 상세22" src="https://github.com/user-attachments/assets/842b6018-e488-45e1-8d61-27e83ad5a573" />


### 📢 공지사항 (Notice)

| 구분          | 메서드 | URI                                 | 설명                                   |
|---------------|--------|-------------------------------------|----------------------------------------|
| 목록 페이지   | GET    | /notice/list                        | 공지사항 목록 (Thymeleaf 렌더링)        |
| 목록 JSON     | GET    | /notice/getList.do                  | AJAX 공지 목록 (페이징 포함)            |
| 상세 조회     | GET    | /notice/detail.do                   | 공지 상세보기                           |
| 작성 폼       | GET    | /notice/write.form                  | 공지 등록 폼                            |
| 등록 처리     | POST   | /notice/addNotice.do                | 이미지 포함 공지 등록 처리              |
| 수정 폼       | POST   | /notice/edit.form                   | 공지 수정 폼 페이지 반환                |
| 수정 처리     | POST   | /notice/modifyNotice.do             | 공지 수정 처리                          |
| 삭제 처리     | POST   | /notice/remove.do                   | 공지 삭제 및 이미지 정리 처리           |
| 조회수 증가   | POST   | /notice/increaseHit.do              | 조회수 증가 (AJAX 요청용)               |
| 이미지 업로드 | POST   | /notice/imageUpload.do              | CKEditor 이미지 업로드 처리             |

**특징**

- CKEditor에서 `<img>` 자동 추출 후 서버 저장
- 이미지 경로 DB 관리 및 공지에 매핑

---

### 🗓 휴가 예약 (Reservation)

| 구분         | 메서드 | URI                    | 설명                                      |
|--------------|--------|-------------------------|-------------------------------------------|
| 예약 신청     | POST   | /reservation/request    | 사용자 예약 요청 처리                     |
| 내 예약 목록  | GET    | /reservation/user       | 사용자 개인 예약 목록 페이지              |
| 월별 예약 조회| GET    | /reservation/list       | 캘린더용 월별 예약 데이터(JSON) 조회      |
| 대기 목록     | GET    | /reservation/pending    | 관리자 승인 대기 목록 페이지              |
| 승인 처리     | POST   | /reservation/approve    | 관리자 예약 승인 처리 (SMS 발송 포함)     |
| 거절 처리     | POST   | /reservation/reject     | 관리자 예약 거절 처리 (SMS 발송 포함)     |
| 전체 목록     | GET    | /reservation/all        | 전체 예약 목록 조회 (JSON)                |

**특징**

- 승인/거절 시 실시간 SMS 전송
- 달력 연동 월별 예약 내역 조회 제공
- 관리자/사용자 권한 분리 처리

---

### 🎉 이벤트 (Event)

| 구분 | 메서드 | URI | 설명 |
| --- | --- | --- | --- |
| 등록 | `POST` | `/event/upload` | 이미지 포함 이벤트 등록 |
| 전체 목록 | `GET` | `/event` | 모든 이벤트 리스트 |
| 삭제 | `DELETE` | `/event/{eventNo}` | 이벤트 삭제 처리 |

**특징**

- 대표/진행중/종료 상태별 뱃지 표시
- 이미지 업로드 + D-Day 계산 표시
- Infinite Scroll 구현 완료

---

### 🎥 신입 가이드 (Guide : YouTube 영상)

| 구분 | 메서드 | URI | 설명 |
| --- | --- | --- | --- |
| 목록 조회 | `GET` | `/guide/list`      | 영상 목록 페이징 |
| 상세 조회 | `GET` | `/guide/{guideNo}` | 상세 정보 |
| 등록 | `POST` | `/guide/add` | YouTube ID 등록 |
| 삭제 | `DELETE` | `/guide/delete/{guideNo}` | 가이드 삭제(추후 구현) |

**특징**

- YouTube API로 영상 정보 자동 수집
- 등록 시 제목/설명/ID DB에 저장

---

### 👤 사용자 (User)

| 구분 | 메서드 | URI | 설명 |
| --- | --- | --- | --- |
| 로그인 페이지 | `GET` | `/user/login.form` | 로그인 UI 이동 |
| 로그인 처리 | `POST` | `/user/login` | 세션 로그인 처리 |
| 로그아웃 | `GET` | `/user/logout` | 로그아웃 처리 및 메인 이동 |

**특징**

- 세션 기반 로그인 유지
- 로그인 후 메인 리다이렉션

---

## 📁 프로젝트 폴더 구조 요약

```

📦 /src/main/java/com/pyj/paris
 ┣ 📂 config
 ┣ 📂 controller
 ┣ 📂 service
 ┣ 📂 dao
 ┣ 📂 dto
 ┣ 📂 util
 ┗ 📄 ParisApplication.java

```

---

## 📌 전체 특징

-  **도메인별 Controller / Service / Mapper / DTO 구조 분리**
-  **RESTful + Thymeleaf 하이브리드 구성**
-  **실시간 SMS 알림 / 이미지 업로드 / Infinite Scroll**
-  **YouTube API 활용한 실시간 영상 정보 등록**
