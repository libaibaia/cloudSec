package com.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.domain.Key;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;




public class Tools {
    public static final Integer maxBucketNum = 1000;
    public static ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(5);

    public static void stopALLTask() {
        BlockingQueue<Runnable> queue = executorService.getQueue();
        for (int i = 0; i < queue.size(); i++) {
            executorService.remove(queue.poll());
        }
    }
    public static String[] getBucketName(String bucketName){
        return bucketName!=null ? bucketName.split(",") : null;
    }

    public static File createZipFile(List<File> files,String bucketName){
        File zip = ZipUtil.zip(FileUtil.createTempFile(bucketName, ".zip", true), false, files.toArray(new File[0]));
        for (File file : files) {
            FileUtil.del(file);
        }
        return zip;
    }

    public static List<Key> readExcel(File file){
        List<Key> keys = new ArrayList<>();
        EasyExcel.read(file, Key.class, new PageReadListener<Key>(keys::addAll)).sheet().doRead();
        return keys;
    }

}
