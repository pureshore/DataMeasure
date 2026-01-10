#!/bin/bash
mkdir -p src/main/resources/static/{css,js,fonts}

# 下载Bootstrap CSS
curl -o src/main/resources/static/css/bootstrap.min.css https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css

# 下载Bootstrap JS
curl -o src/main/resources/static/js/bootstrap.bundle.min.js https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js

# 下载Bootstrap Icons字体
curl -o src/main/resources/static/fonts/bootstrap-icons.woff2 https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/fonts/bootstrap-icons.woff2

echo "资源下载完成"