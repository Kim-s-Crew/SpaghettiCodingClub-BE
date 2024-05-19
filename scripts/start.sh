#!/bin/bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/kims-spaghetti.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

# AWS CLI를 사용하여 AWS Systems Manager Parameter Store에서 파라미터 값을 불러옵니다
export PROD_DB_URL=$(aws ssm get-parameter --name "/prod/spaghetti-app/db/url" --with-decryption --query "Parameter.Value" --output text)
export PROD_DB_USER=$(aws ssm get-parameter --name "/prod/spaghetti-app/db/user" --with-decryption --query "Parameter.Value" --output text)
export PROD_DB_PASSWORD=$(aws ssm get-parameter --name "/prod/spaghetti-app/db/password" --with-decryption --query "Parameter.Value" --output text)
export JWT_SECRET_KEY=$(aws ssm get-parameter --name "/prod/spaghetti-app/jwt/secret" --with-decryption --query "Parameter.Value" --output text)

# 환경 변수 설정 확인
{
  echo "PROD_DB_URL=$PROD_DB_URL"
  echo "PROD_DB_USER=$PROD_DB_USER"
  echo "PROD_DB_PASSWORD=$PROD_DB_PASSWORD"
  echo "JWT_SECRET_KEY=$JWT_SECRET_KEY"
} >> $DEPLOY_LOG

TIME_NOW=$(date +%c)

# 기존 애플리케이션 종료
CURRENT_PID=$(pgrep -f $JAR_FILE)
if [ -n "$CURRENT_PID" ]; then
    echo "$TIME_NOW > 기존 애플리케이션 종료: $CURRENT_PID" >> $DEPLOY_LOG
    kill -15 $CURRENT_PID
    sleep 5
fi

# build 파일 복사
BUILD_JAR=$(ls $PROJECT_ROOT/build/libs/*.jar)
if [ -z "$BUILD_JAR" ]; then
    echo "$TIME_NOW > 빌드 파일이 존재하지 않습니다." >> $DEPLOY_LOG
    exit 1
fi
echo "$TIME_NOW > $BUILD_JAR 파일 복사" >> $DEPLOY_LOG
cp $BUILD_JAR $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
source ~/.bashrc
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

# 현재 실행중인 프로세스 ID 출력
CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
