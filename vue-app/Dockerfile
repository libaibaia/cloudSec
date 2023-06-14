#!/bin/bash
#使用 Node.js v16 作为基础镜像
FROM node:16 AS build
ENV IMAGE_NAME vue-app:latest
#将 Docker 容器的工作目录设置为 /app
WORKDIR /app
#复制 package.json 和 package-lock.json ，并安装依赖
COPY package*.json ./

RUN npm config set registry https://registry.npm.taobao.org

RUN npm install
COPY . .
# 替换后端接口
RUN sed -i "s|VITE_AXIOS_BASE_URL.*|VITE_AXIOS_BASE_URL = 'http:\/\/$API_IP\/'|g" .env.production

#编译 Vue.js 应用程序以生产环境
RUN npm run build

#使用 Nginx 作为服务应用程序的基础镜像
FROM nginx:stable-alpine

#删除默认的 Nginx 配置文件
RUN cat /docker-entrypoint.sh
RUN rm /etc/nginx/conf.d/default.conf
ARG API_IP
#从 Build 阶段复制 Nginx 配置文件
COPY nginx.conf /etc/nginx/conf.d
CMD echo "API_IP=${API_IP}"
RUN sed -i -e 's/API_IP/'"$API_IP"'/' /etc/nginx/conf.d/nginx.conf
RUN cat /etc/nginx/conf.d/nginx.conf
#从 Build 阶段复制已构建的 Vue.js 应用程序
COPY --from=build /app/dist /usr/share/nginx/html

#暴露默认的 Nginx 端口（80）
EXPOSE 80

#在基于此映像的容器中运行时启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]
