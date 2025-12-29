# ---- ai-server (python deps 빌드 용) ----
FROM python:3.11-slim AS ai

WORKDIR /ai-server

COPY ai-server/ ./

# requirements 만 미리 resolve (캐시용)
RUN pip install --upgrade pip && \
    pip install --prefix=/python-deps -r requirements.txt


# ---- 최종 런타임 (java + python 같이) ----
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# 1) 파이썬 설치
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    ln -s /usr/bin/python3 /usr/local/bin/python && \
    rm -rf /var/lib/apt/lists/*

# 2) ai-server 코드 복사
WORKDIR /ai-server
COPY ai-server/ ./

# 3) 파이썬 패키지 설치 (컨테이너 내에서 다시)
RUN pip3 install --upgrade pip && \
    pip3 install -r requirements.txt

# 4) 자바 앱 복사
WORKDIR /app
COPY 5core/build/libs/5core-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
