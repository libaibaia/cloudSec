package com.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;


public class Tools {
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
        long current = DateUtil.current();
        String path = "../../" + current;
        File dir = FileUtil.mkdir(path);
        for (File file : files) {
            FileUtil.moveContent(file,dir,true);
        }
        return ZipUtil.zip(dir.getPath(), FileUtil.createTempFile(bucketName,".zip", true).getPath());
    }
}
