package com;

import cn.hutool.core.io.FileUtil;
import com.common.Tools;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.util.ResourceUtils;

import javax.annotation.PreDestroy;
import java.io.*;

@SpringBootApplication
@MapperScan({"com.mapper"})
public class ApplicationStart {
    public static void main(String[] args) throws FileNotFoundException {
        initDBFile();
        System.out.println("开始启动应用");
        SpringApplication.run(ApplicationStart.class, args);
    }
    @PreDestroy
    public void destroy(){
        Tools.stopALLTask();
    }
    private static void initDBFile(){
        System.out.println("初始化基础数据。。");
        InputStream inputStream = ApplicationStart.class.getClassLoader().getResourceAsStream("DB/DB.db");
        // 指定目标文件路径
        File file = FileUtil.newFile("DB.db");
        if (!file.exists()) {
            // 将输入流写入目标文件
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                System.out.println("初始化失败：" + e.getMessage());
                throw new RuntimeException(e);
            }
            System.out.println("数据库复制成功！");
        } else {
            System.out.println("数据库文件已存在，跳过复制。");

        }
    }
}
