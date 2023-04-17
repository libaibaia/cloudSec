package com.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;


public class Tools {
    public static ExecutorService executorService = new ScheduledThreadPoolExecutor(5);
    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

}
