#!/bin/bash

# 定义日志目录和基础文件名
LOG_DIR="/var/log/deploy"
LOG_BASE="flight-api"
LOG_FILE="$LOG_DIR/${LOG_BASE}-$(date +%Y%m%d_%H%M%S).log"  # 带时间戳的日志文件

# 创建日志目录（如果不存在）
mkdir -p "$LOG_DIR"

# 定义日志函数，添加时间戳并输出到日志文件
log() {
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    local message="[$timestamp] $1"

    # 同时输出到标准输出和日志文件
    echo "$message"
    echo "$message" >> "$LOG_FILE"
}

# 部署脚本
set -e

# 捕获脚本错误并记录
trap 'log "错误: 脚本执行失败，行号: $LINENO，命令: $BASH_COMMAND"' ERR

log "==== 开始自动部署 ===="
cd ~/app/flight-api || { log "错误: 项目目录不存在"; exit 1; }

# 拉取最新代码
log "---- 拉取最新代码 ----"
git fetch origin dev-v0.0.1  # 获取远程更新
git reset --hard origin/dev-v0.0.1  # 强制重置到远程状态

# 编译项目
log "---- 编译项目 ----"
mvn clean package -DskipTests

# 构建Docker镜像
log "---- 构建Docker镜像 ----"
docker build -t flight-api:latest .

# 停止并删除旧容器
log "---- 停止并删除旧容器 ----"
docker-compose down flight-api

# 启动新容器
log "---- 启动新容器 ----"
docker-compose up flight-api -d

log "==== 部署完成 ==== 日志文件: $LOG_FILE"
