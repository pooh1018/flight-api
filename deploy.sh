#!/bin/bash

# 部署脚本
set -e

echo "==== 开始自动部署 ===="
cd ~/app/flight-api

# 拉取最新代码
echo "---- 拉取最新代码 ----"
git pull origin dev-v0.0.1

# 编译项目
echo "---- 编译项目 ----"
mvn clean package -DskipTests

# 构建Docker镜像
echo "---- 构建Docker镜像 ----"
#docker build -t flight-api:$(date +%s) .
docker build -t flight-api:latest .

# 停止并删除旧容器
echo "---- 停止并删除旧容器 ----"
#cd ~/docker
docker-compose down flight-api

# 启动新容器
echo "---- 启动新容器 ----"
docker-compose up flight-api -d

echo "==== 部署完成 ===="
