package com.zereao.wechat.commom.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程池
 *
 * @author Zereao
 * @version 2018/12/21  18:53
 */
public class ThreadPoolUtils {
    private static ExecutorService executor = new ThreadPoolExecutor(
            5,
            10,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("wechat-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void execute(Runnable task) {
        executor.execute(task);
    }
}
