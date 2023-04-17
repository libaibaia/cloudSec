# cloudSec
# AK/SK泄露利用工具
***前端采用vue3,基于buildadmin模板，后端springboot，原接口调用厂商的SDK***
## 关于使用(目前接入了腾讯云的所以下述功能接口，阿里云目前仅支持服务器资源检测，其余厂商需要慢慢补充)
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
3. 存储桶，暂时没有加入文件操作的功能，后期会加入
![image](https://user-images.githubusercontent.com/108923559/232521771-cfb4230c-231f-4093-b433-e819eb7b5230.png)
4. 控制台用户，需要在ak/sk管理处添加控制台用户
![image](https://user-images.githubusercontent.com/108923559/232523622-87daeb12-21dc-49f6-a604-d02b41f0bc64.png)
5. 数据库
- 主要是对数据库的操作，如开通外网访问，创建用户。
- 右边按钮对应-->打开外网访问-->关闭外网访问-->创建数据库用户
![image](https://user-images.githubusercontent.com/108923559/232523914-4d6da393-e83c-46f2-8acf-48bc3cc2f4c6.png)
![image](https://user-images.githubusercontent.com/108923559/232524570-d9e42b57-edea-4031-b0a1-eadf1184daf2.png)

## docker部署
```yaml
version: "3"
services:
  mysql:
    image: registry.cn-qingdao.aliyuncs.com/lx_project/project:mysql
    container_name: mysql
    volumes:
      - /home/mysql/data:/var/lib/mysql # 挂载目录，可以自定义
    environment:
      MYSQL_ROOT_PASSWORD: 123456
    ports:
      - "3306:3306" 
  app:
    image: registry.cn-qingdao.aliyuncs.com/lx_project/project:app
    links:
      - "mysql:db"
    container_name: app
    ports:
      - "8000:8000"
  web:
    image: registry.cn-qingdao.aliyuncs.com/lx_project/project:web
    container_name: web
    environment:
      API_IP: 192.168.61.129 #更改此处为本机IP
      API_PORT: 8000
    ports:
      - "80:80"
# 其余部分无需更改，默认即可
```

启动脚本
```bash
docker-compose up -d
docker exec -it web /bin/bash "/home/Iinit.sh" #因为需要更改前端API接口地址，所以需要执行这个脚本，也可以docker-compose启动后进入容器手动执行
exit
echo "start ok"
```

## 本地部署
- 数据库mysql5.7
- jdk8
- node 16.16
前端项目地址：https://github.com/libaibaia/cloudSecVue/tree/master
- 步骤：
1. 编译后端项目（将application中的mysql改为本地mysql地址） mnv package
2. 前端项目打包,打包前更改.env.production文件中的VITE_AXIOS_BASE_URL为本机IP，然后，npm install --> npm run build
3. 将编译后的dist文件复制到nginx目录下
4. 启动后端java -jar cloudSec.jar
5. 访问nginx80端口登录，默认账号密码admin/admin123。
