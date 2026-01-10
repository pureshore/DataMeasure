@echo off
set APP_NAME=cookie-manager
set JAR_NAME=cookie-manager-1.0.0.jar
set PROFILE=dev

echo Starting %APP_NAME%...
echo Profile: %PROFILE%

java -server -Xms512m -Xmx1024m -XX:+UseG1GC ^
  -Dspring.profiles.active=%PROFILE% ^
  -jar %JAR_NAME%

pause