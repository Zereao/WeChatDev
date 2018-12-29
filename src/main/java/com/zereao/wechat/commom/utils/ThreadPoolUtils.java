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
            10,
            20,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("wechat-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void execute(Runnable task) {
        executor.execute(task);
    }

    public static <T> T submit(Callable<T> task) throws ExecutionException, InterruptedException {
        Future<T> future = executor.submit(task);
        return future.get();
    }
}
