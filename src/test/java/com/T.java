package com;

import com.common.Tools;

import java.util.concurrent.atomic.AtomicInteger;

public class T {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(3);
        Tools.executorService.submit(() -> {atomicInteger.decrementAndGet();});
        while (true){
            System.out.println(atomicInteger.get());
        }
    }
}
