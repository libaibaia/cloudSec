package com.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
