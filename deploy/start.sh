#!/bin/bash

# 应用名称
APP_NAME="cookie-manager"
JAR_NAME="cookie-manager-1.0.0.jar"

# Java选项
JAVA_OPTS="-server -Xms512m -Xmx1024m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"

# 配置文件
PROFILE="prod"

echo "Starting $APP_NAME..."
echo "Profile: $PROFILE"
echo "Java Options: $JAVA_OPTS"

# 启动应用
nohup java $JAVA_OPTS \
  -Dspring.profiles.active=$PROFILE \
  -Dlogging.config=config/logback-$PROFILE.xml \
  -jar $JAR_NAME > nohup.out 2>&1 &

echo "$APP_NAME started with PID: $!"