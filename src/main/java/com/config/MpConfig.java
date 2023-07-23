package com.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;


@Configuration
public class MpConfig implements MetaObjectHandler {
    /**
     * 插入时的填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", getTimeStr(), metaObject);
        this.setFieldValByName("updateTime", getTimeStr(), metaObject);
    }

    /**
     * 更新时的填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", getTimeStr(), metaObject);
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("dialect", "Mysql");
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    public static String getTimeStr() {
        String string = new Date().toString();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(string, inputFormatter);
            return dateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            return string;
        }
    }
}
