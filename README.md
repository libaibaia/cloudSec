# cloudSec
# 云安全-AK/SK泄露利用工具
- 注意：如果本地使用linux搭建，openjdk需要安装字体库，建议使用oraclejdk
- 前端采用vue3,基于buildadmin模板，后端springboot，原接口调用厂商的SDK
- 如果有BUG请提交issue
- ***提示：某些功能会对目标产品造成影响，如密钥对绑定，会导致重启，请慎重使用！！！***
- 另外权限信息获取目前只支持阿里云腾讯与
## 功能概览
<table>
  <tr>
    <th>厂商</th>
    <th>产品</th>
    <th>功能</th>
    <th>备注</th>
  </tr>
  
  <tr>
    <td rowspan="4">七牛云</td>
    <td>云服务器</td>
    <td>列出云服务器/绑定密钥对</td>
    <td>/</td>
  </tr>
  
  <tr>
    <td>云数据库</td>
    <td>/</td>
    <td>/</td>
  </tr>
  
  <tr>
    <td>存储桶</td>
    <td>列出文件/单文件下载链接生成/上传文件/导出存储桶所有文件列表</td>
    <td>因为下载文件过多收费问题，所以改成了导出所有文件列表，然后通过文件名自己筛选即可，界面支持前缀搜索，支持1000条数据实时预览</td>
  </tr>
  
  <tr>
    <td>控制台用户</td>
    <td>/</td>
    <td>/</td>
  </tr>
  
  <tr>
    <td rowspan="4">阿里云</td>
    <td>云服务器</td>
    <td>列出云服务器/执行命令/绑定(还原)密钥对</td>
    <td>密钥对操作需要重启服务器</td>
  </tr>
  
  <tr>
    <td>云数据库</td>
    <td>获取数据库资源/创建用户/开通或关闭外网访问</td>
    <td>账号继承父账号权限</td>
  </tr>
  
  <tr>
    <td>存储桶</td>
    <td>列出文件/单文件下载链接生成/上传文件/导出存储桶所有文件列表</td>
    <td>因为下载文件过多收费问题，所以改成了导出所有文件列表，然后通过文件名自己筛选即可，界面支持前缀搜索，支持1000条数据实时预览</td>
  </tr>
  
  <tr>
    <td>控制台用户</td>
    <td>创建控制台用户</td>
    <td>默认管理员权限</td>
  </tr>
  
  <tr>
    <td rowspan="4">腾讯云</td>
    <td>云服务器</td>
    <td>列出云服务器/执行命令/绑定(还原)密钥对</td>
    <td>密钥对操作需要重启服务器</td>
  </tr>
  
  <tr>
    <td>云数据库</td>
    <td>获取数据库资源/创建用户/开通或关闭外网访问</td>
    <td>账号继承父账号权限</td>
  </tr>
  
  <tr>
    <td>存储桶</td>
    <td>列出文件/单文件下载链接生成/上传文件/导出存储桶所有文件列表</td>
    <td>因为下载文件过多收费问题，所以改成了导出所有文件列表，然后通过文件名自己筛选即可，界面支持前缀搜索，支持1000条数据实时预览</td>
  </tr>
  
  <tr>
    <td>控制台用户</td>
    <td>创建控制台用户</td>
    <td>默认父账号权限</td>
  </tr>
  
  <tr>
    <td rowspan="4">亚马逊云</td>
    <td>云服务器</td>
    <td>列出云服务器</td>
    <td>/</td>
  </tr>
  
  <tr>
    <td>云数据库</td>
    <td>列出数据库资源</td>
    <td>/</td>
  </tr>
  
  <tr>
    <td>存储桶</td>
    <td>列出文件/单文件下载链接生成/上传文件/导出存储桶所有文件列表</td>
    <td>因为下载文件过多收费问题，所以改成了导出所有文件列表，然后通过文件名自己筛选即可，界面支持前缀搜索，支持1000条数据实时预览</td>
  </tr>
  
  <tr>
    <td>控制台用户</td>
    <td>创建控制台用户</td>
    <td>默认最高权限</td>
  </tr>
</table>

## 使用方式
- 默认检测是10个线程
### 添加AK/SK
- 右边按钮对应key更新编辑，任务启动，控制台用户创建
- 添加后选择立即检测或手动执行任务
- 可选择更新时导出key
- 一键停止/启动所有任务
![image](https://github.com/libaibaia/cloudSec/assets/108923559/da0d8ac3-9219-43bc-aec8-c847abde3a3e)
### 云服务器
- 对应命令执行，密钥对操作
![image](https://github.com/libaibaia/cloudSec/assets/108923559/4bc30291-77c4-49e8-856a-814d3ab270df)
![image](https://github.com/libaibaia/cloudSec/assets/108923559/d61b4c05-4135-4674-822a-b27d7bba7652)
### 存储桶
- 对应文件上传，导出文件列表（excel格式）
![image](https://github.com/libaibaia/cloudSec/assets/108923559/0a6a4762-7170-4db4-aa6b-6fa3a114e05e)
- 点击上传然后选择文件列表，可预览1000条数据，点击下载可单独下载文件
![image](https://github.com/libaibaia/cloudSec/assets/108923559/238b5274-5992-48f3-95b3-c0cfbd15a4ff)
### 控制台用户
- 创建的控制台用户将在这里显示
![image](https://github.com/libaibaia/cloudSec/assets/108923559/c1ad8360-702d-4b19-92b0-89b1d734891d)
### 数据库
- 按钮对应打开/关闭外网，创建数据库账号
![image](https://github.com/libaibaia/cloudSec/assets/108923559/1b12e232-b3b9-45f6-a3c2-50973ef45a47)
### 文件下载列表
- 此处对应导出存储桶的文件列表表格，状态成功后可下载
![image](https://github.com/libaibaia/cloudSec/assets/108923559/028e6c9c-3163-4d03-8e2d-7ca041ff507e)
### 导入key列表，为了更新不丢失key
![image](https://github.com/libaibaia/cloudSec/assets/108923559/1a078f96-27e5-421b-bba7-dd2df8b84e48)

## docker-compose部署
- 注意：如果出现 ***ERROR: The Compose file './docker-compose.yaml' is invalid because:Unsupported config option for services: 'db'*** 类似错误请升级docker-compose版本，我本地使用的是1.29+版本。
- 升级
```bash
$ sudo curl -L "https://github.com/docker/compose/releases/download/{version}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

$ sudo chmod +x /usr/local/bin/docker-compose
```
- 如果更新需要删除原有镜像，密钥可以导出，然后再导入
```yaml
services:
  java-app:
    container_name: java-app
    image: registry.cn-hangzhou.aliyuncs.com/lx_project/cloud:java-app-latest
    environment:
      DB_PASSWORD: 123456
    depends_on:
      - db
  vue-web:
    container_name: vue-web
    image: registry.cn-hangzhou.aliyuncs.com/lx_project/cloud:vue-app-latest
    ports:
      - "80:80"
    environment:
      - API_IP=192.168.61.131
    depends_on:
      - java-app
  db:
    container_name: db
    image: registry.cn-hangzhou.aliyuncs.com/lx_project/cloud:db-latest
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 123456
    volumes:
      - /home/cloud/data:/var/lib/mysql
```

启动脚本
```bash
docker-compose up -d
#然后访问http://<IP>/admin/login
#默认账号密码admin/admin123
```

## 本地部署
- 数据库mysql5.7
- jdk8
- node 16.16
前端项目地址：[https://github.com/libaibaia/vue-web](https://github.com/libaibaia/web-vue)
- 步骤：
1. 编译后端项目（将application中的mysql改为本地mysql地址） mvn package
2. 前端项目打包,打包前更改.env.production文件中的VITE_AXIOS_BASE_URL为本机IP，然后，npm install --> npm run build
3. 将编译后的dist文件复制到nginx目录下
4. 启动后端java -jar cloudSec.jar
5. 访问nginx80端口登录，默认账号密码admin/admin123。
## 本地构建docker
1. 编译项目放在java-app/target目录下
2. 下载jdk8，需要oracle版本
3. 解压WEB.ZIP文件到当前目录
4. 执行docker-compose -f docker-compose-build.yaml up -d --build 构建镜像并启动
