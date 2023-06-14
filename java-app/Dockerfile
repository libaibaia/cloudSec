#!/bin/bash
FROM ubuntu:latest


# 下载 Oracle JDK8 安装包
ADD jdk-8u351-linux-x64.tar.gz /usr/local/

# 配置 JDK8 环境变量
ENV JAVA_HOME /usr/local/jdk1.8.0_351
ENV PATH ${JAVA_HOME}/bin:$PATH


# 设置时区为上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用程序 Jar 包
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /home/app.jar

# 在容器中运行应用程序
CMD ["java", "-jar", "/home/app.jar"]
