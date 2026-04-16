# 5core

## 프로젝트 소개
현대자동차의 자동차 판매 관리 및 AI지능형 CRM 플랫폼입니다.

## 데모 영상
[![데모 영상](https://img.youtube.com/vi/_6k37G50p34/0.jpg)](https://youtu.be/_6k37G50p34)

## 개발기간
2025.11.19 ~ 2025.12.9

## 기술스택
- Backend: Spring Boot, Spring Data JPA
- Language: Java 21, Python
- Frontend: HTML5/CSS3, Thymeleaf, JavaScript, jQuery
- Dashboard: Streamlit, Plotly, Matplotlib
- Database: MySQL
- API:
  - Gemini API
  - Naver/Kakao Login API
  - Google MAps Platform
  - 전기차 충전소 실시간 API
  - 표준 행정구역 API (고객 상담 시 지역 선택)
- Infra: Docker, AWS

## 핵심 기능
- 관리자: 딜러·차량·실적·AI 예측 통합 관리  
- 딜러: 차량·고객·상담 진행상황 관리  
- 고객: 차량 조회, 문의, 상담 신청  
- 지도/충전소 조회: Google Maps Platform + 전기차 충전소 API + 행정구역 API 연동  
- AI 챗봇: Gemini API 기반 자동 상담 지원, 차량 추천
- AI 수요예측: Python + Streamlit/Plotly/Matplotlib로 수요·판매량 분석 및 시각화

## 핵심 기능
- 관리자: 딜러·차량·실적·AI 예측 통합 관리  
- 딜러: 차량·고객·상담 진행상황 관리  
- 고객: 차량 조회, 문의, 상담 신청  
- 고객 상담 지역 선택: 표준 행정구역 API를 활용해 고객이 상담 요청 시 거주 지역을 선택할 수 있도록 구성  
- 지도/충전소 조회: Google Maps Platform + 전기차 충전소 API 연동  
- AI 챗봇: Gemini API 기반 자동 상담 지원, 차량 추천
- AI 수요예측: Python + Streamlit/Plotly/Matplotlib로 수요·판매량 분석 및 시각화

## 내가 맡은 기능

- **팀 리딩 및 기술 리뷰**  
  팀장 역할을 수행하며, 팀원들의 기능 구현 중 어려운 문제가 생기면 함께 고민하고 해결 방향을 논의하며  
  전체 개발 흐름과 품질을 조율했습니다.

- **Security 및 DB 모델링**  
  회원·딜러·고객·상담·구매 관련 엔터티를 설계하고, ER 관계를 정리하며  
  필요한 필드를 수정·보완해 DB 구조를 최적화했습니다.

- **계정 관리 기능 전체 담당**  
  - 회원가입, 회원 상세정보 조회·수정·삭제 기능 구현  
  - Naver/Kakao 소셜 로그인 API 연동(OAuth2 기반 로그인)  
  - 보안 관련 설정과 권한 처리 정의

- **딜러 전용 기능 전부 담당**  
  - 딜러 프로필 페이지 개발  
  - 고객 리스트 및 상담 신청 명단 관리  
  - 상담 신청 건에서 상담 여부 결정, 상담 후 구매 여부 체크 및 구매 반영  
  - 구매 관리 기능(구매 내역 조회·정리 등)

- **프로필 페이지 AI 그래프**  
  딜러 프로필 페이지에 **AI 월간 판매량, 차종별 판매량 그래프**를 표시해  
  개별 딜러의 실적을 시각적으로 확인할 수 있도록 구성했습니다.

- **관리자용 AI 기능**  
  관리자 페이지에서 **AI 글로벌 판매 실적, 국내·북미 수요예측, 차량별 판매 실적**을 시각화하여  
  전사적인 판매 트렌드와 예측을 한 번에 조회할 수 있는 대시보드를 구현했습니다.

