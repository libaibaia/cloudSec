# cloudSec
# 云安全-AK/SK泄露利用工具
***注意：部分代码中由于时间关系未做权限控制，存在越权，建议本地搭建使用即可***
***前端采用vue3,基于buildadmin模板，后端springboot，原接口调用厂商的SDK***
## 关于使用(目前接入了腾讯云的所以下述功能接口，阿里云已更新加入，其余厂商需要慢慢补充)
## 关于后续添加的厂商
- 亚马逊云
- 七牛云
- 华为云
- 微软云
- 谷歌
以上是后续的更详细方向，有点多，可能一时半会更新不完
***注：如果页面白屏刷新浏览器即可，因为热更新的原因导致，后期会解决。***
1. AK/SK管理
![image](https://user-images.githubusercontent.com/108923559/232522170-4e0bf7ee-067c-4401-9ed1-f7f51abfe5a5.png)
***右边图标对应-->重新检测资源及权限（也就是重新遍历资源信息）-->添加控制台用户（通过控制台登录）-->获取策略列表（需要当前账号绑定了策略）***
![image](https://user-images.githubusercontent.com/108923559/232522409-0f63dac0-949f-4c62-9813-8fea0e1f4461.png)
2. 云服务器相关
- 对应图标-->执行命令-->绑定密钥对（由于某些secret不支持运维助手或tat助手权限，但是拥有服务器完全的操作权限，因此可以通过绑定密钥的方式来获取目标主机权限，阿里云需要提供本地客户端的私钥，腾讯创建完毕将私钥导入本地即可）
![image](https://user-images.githubusercontent.com/108923559/232520276-bd9e23fc-eab2-4af1-ad99-ecb5d3bb834c.png)
![image](https://user-images.githubusercontent.com/108923559/232520972-8deed19a-f5b2-4fdd-b5fd-156ea933ded7.png)
![image](https://user-images.githubusercontent.com/108923559/232521203-c0320ef8-0df3-4f3a-b9fe-c3afe8aaf5f7.png)
3. 存储桶，文件操作，打包下载所有文件及单独文件url生成
- 预览文件列表
![image](https://user-images.githubusercontent.com/108923559/232521771-cfb4230c-231f-4093-b433-e819eb7b5230.png)
![image](https://github.com/libaibaia/cloudSec/assets/108923559/ca40b9f8-b1be-4dd9-8720-abe35ce8d687)
- 上传
![image](https://github.com/libaibaia/cloudSec/assets/108923559/12981dbb-d4c3-4646-87ff-bbdc81d1e3a1)
4. 控制台用户，需要在ak/sk管理处添加控制台用户
![image](https://user-images.githubusercontent.com/108923559/232523622-87daeb12-21dc-49f6-a604-d02b41f0bc64.png)
5. 数据库
- 主要是对数据库的操作，如开通外网访问，创建用户。
- 右边按钮对应-->打开外网访问-->关闭外网访问-->创建数据库用户
![image](https://user-images.githubusercontent.com/108923559/232523914-4d6da393-e83c-46f2-8acf-48bc3cc2f4c6.png)
![image](https://user-images.githubusercontent.com/108923559/232524570-d9e42b57-edea-4031-b0a1-eadf1184daf2.png)

## docker部署
```yaml
version: '3.8'

services:
  java-app:
    container_name: app
    image: registry.cn-hangzhou.aliyuncs.com/lx_project/cloud:java-app
    environment:
      DB_PASSWORD: password
    depends_on:
      - db
  vue-web:
    container_name: vue-web
    image: registry.cn-hangzhou.aliyuncs.com/lx_project/cloud:vue-app
    ports:
      - "80:80"
    environment:
      - API_IP=192.168.61.131
    depends_on:
      - java-app
  db:
    container_name: db
    image: registry.cn-hangzhou.aliyuncs.com/lx_project/cloud:db
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: password
    volumes:
      - /home/cloud/data:/var/lib/mysql


```

启动脚本
```bash
docker-compose up -d
```

## 本地部署
- 数据库mysql5.7
- jdk8
- node 16.16
前端项目地址：[https://github.com/libaibaia/vue-web](https://github.com/libaibaia/web-vue)
- 步骤：
1. 编译后端项目（将application中的mysql改为本地mysql地址） mnv package
2. 前端项目打包,打包前更改.env.production文件中的VITE_AXIOS_BASE_URL为本机IP，然后，npm install --> npm run build
3. 将编译后的dist文件复制到nginx目录下
4. 启动后端java -jar cloudSec.jar
5. 访问nginx80端口登录，默认账号密码admin/admin123。
