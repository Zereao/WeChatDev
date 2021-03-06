package com.zereao.wechat.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程池
 *
 * @author Darion Mograine H
 * @version 2018/12/21  18:53
 */
public class ThreadPoolUtils {
    private static ExecutorService executor = new ThreadPoolExecutor(
            10,
            15,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("wechat-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public static ExecutorService getExecutor() {
        return ThreadPoolUtils.executor;
    }

    public static void execute(Runnable task) {
        executor.execute(task);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }
}
