# Auth API — Google OAuth 2.0

## Overview

인증 방식: **Google OAuth 2.0 + JWT (HttpOnly Cookie)**

- 로그인은 브라우저를 Google 로그인 페이지로 보내는 방식 (Redirect-based)
- 로그인 성공 후 서버가 JWT를 `access_token` HttpOnly 쿠키에 저장
- 이후 모든 API 요청은 브라우저가 쿠키를 자동으로 포함
- CSRF 보호를 위해 상태 변경 요청(POST/PUT/DELETE/PATCH)에 `X-XSRF-TOKEN` 헤더 필수

---

## Base URL

```
http://localhost:8080
```

---

## 인증 흐름

```
1. 프론트엔드: 브라우저를 GET /oauth2/authorization/google 로 이동
2. 서버 → Google 로그인 페이지로 redirect
3. 유저가 Google 계정 선택
4. 서버: Google callback 처리 → DB에 User upsert → JWT 생성
5. 서버: Set-Cookie: access_token=<JWT>; HttpOnly; SameSite=Lax
6. 서버: 프론트엔드 redirect-uri 로 redirect (기본값: http://localhost:3000/auth/callback)
7. 프론트엔드 /auth/callback 에서 로그인 상태 처리
```

---

## Endpoints

### 로그인 시작

```
GET /oauth2/authorization/google
```

- 브라우저를 Google 로그인 페이지로 redirect하는 서버 엔드포인트
- **API 호출이 아니라 `window.location.href` 또는 `<a href>` 로 이동**
- 인증 필요 없음 (public)

**사용 예시:**

```typescript
// 로그인 버튼 클릭 핸들러
const handleLogin = () => {
  window.location.href = "http://localhost:8080/oauth2/authorization/google";
};
```

---

### 로그인 완료 후 Callback 도착

로그인 성공 시 서버가 프론트엔드의 아래 URI로 redirect:

```
GET http://localhost:3000/auth/callback
```

이 시점에 `access_token` HttpOnly 쿠키와 `XSRF-TOKEN` 쿠키가 브라우저에 세팅되어 있음.

**`/auth/callback` 페이지에서 할 일:**

```typescript
// pages/auth/callback.tsx 또는 app/auth/callback/page.tsx
// 쿠키는 이미 세팅되어 있으므로 별도 처리 없이 /me 호출로 유저 정보 확인 후 홈으로 이동
useEffect(() => {
  fetch("http://localhost:8080/api/me", { credentials: "include" })
    .then((res) => {
      if (res.ok) router.replace("/");
      else router.replace("/login");
    });
}, []);
```

---

### 로그아웃

```
POST /api/logout
```

- 서버가 `access_token` 쿠키를 만료(Max-Age=0)시켜 삭제
- `X-XSRF-TOKEN` 헤더 필요 (POST 요청이므로)

**사용 예시:**

```typescript
const handleLogout = async () => {
  const csrfToken = getCookie("XSRF-TOKEN");
  await fetch("http://localhost:8080/api/logout", {
    method: "POST",
    credentials: "include",
    headers: { "X-XSRF-TOKEN": csrfToken ?? "" },
  });
  router.replace("/login");
};
```

> **현재 미구현.** 임시 로그아웃은 브라우저 쿠키 수동 삭제로 처리.

---

## 인증이 필요한 API 호출 방법

### GET 요청 (CSRF 헤더 불필요)

```typescript
const res = await fetch("http://localhost:8080/api/articles", {
  credentials: "include", // 쿠키 자동 포함 — 필수
});
```

### POST / PUT / DELETE / PATCH 요청 (CSRF 헤더 필수)

```typescript
// XSRF-TOKEN 쿠키 읽기 유틸
const getCookie = (name: string): string | null => {
  const match = document.cookie.match(new RegExp("(^| )" + name + "=([^;]+)"));
  return match ? decodeURIComponent(match[2]) : null;
};

const res = await fetch("http://localhost:8080/api/subscriptions", {
  method: "POST",
  credentials: "include",
  headers: {
    "Content-Type": "application/json",
    "X-XSRF-TOKEN": getCookie("XSRF-TOKEN") ?? "",
  },
  body: JSON.stringify({ keyword: "Spring Boot" }),
});
```

---

## 쿠키 명세

| 이름 | HttpOnly | SameSite | 용도 |
|------|----------|----------|------|
| `access_token` | ✅ (JS 접근 불가) | Lax | JWT 인증 토큰 (7일) |
| `XSRF-TOKEN` | ❌ (JS 읽기 가능) | Lax | CSRF 방어용 토큰 |

---

## 에러 응답

| 상황 | HTTP 상태 | 동작 |
|------|-----------|------|
| 쿠키 없음 / 토큰 만료 | `401` | `/login` 으로 redirect |
| CSRF 토큰 누락 또는 불일치 | `403` | 요청 거부 |

**401 공통 처리 예시 (fetch wrapper):**

```typescript
const apiFetch = async (url: string, options?: RequestInit) => {
  const res = await fetch(`http://localhost:8080${url}`, {
    ...options,
    credentials: "include",
  });

  if (res.status === 401) {
    window.location.href = "/login";
    return;
  }

  return res;
};
```

---

## Axios 사용 시 전역 설정

```typescript
// lib/axios.ts
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // 쿠키 자동 포함
  xsrfCookieName: "XSRF-TOKEN",      // Spring이 세팅하는 CSRF 쿠키 이름
  xsrfHeaderName: "X-XSRF-TOKEN",    // Spring이 기대하는 CSRF 헤더 이름
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

export default api;
```

> Axios는 `xsrfCookieName` / `xsrfHeaderName` 설정만으로 CSRF 토큰을 자동으로 읽어서 헤더에 추가합니다.

---

## 환경변수 (프론트엔드)

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_OAUTH2_REDIRECT_URI=http://localhost:3000/auth/callback
```

---

## User 객체 (응답 예시)

> `/api/me` 구현 시 예정된 응답 형태

```json
{
  "id": 1,
  "email": "user@gmail.com",
  "name": "홍길동",
  "avatar": "https://lh3.googleusercontent.com/...",
  "provider": "GOOGLE"
}
```
