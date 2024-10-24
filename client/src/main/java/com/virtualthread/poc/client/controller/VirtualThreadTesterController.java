package com.virtualthread.poc.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class VirtualThreadTesterController {

    private static final int TASK_COUNT = 100;
    private static final int SLEEP_TIME_MS = 2000; // 2 seconds for blocking

    @GetMapping("/test/diff")
    public String testNormalThreads() throws InterruptedException {
        String result = "";

        // Create an ExecutorService using normal platform threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        long startTime = System.currentTimeMillis();

        // Submit tasks to the thread pool
        for (int i = 0; i < TASK_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(SLEEP_TIME_MS); // Simulate blocking task
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Shutdown the executor and wait for tasks to finish
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();
        result = "Normal threads completed in " + (endTime - startTime) + " ms" + "\n";


        // Create an ExecutorService using virtual threads
        ExecutorService virtualThreadPerTaskExecutor = Executors.newVirtualThreadPerTaskExecutor();

        startTime = System.currentTimeMillis();

        // Submit tasks to the virtual thread executor
        for (int i = 0; i < TASK_COUNT; i++) {
            virtualThreadPerTaskExecutor.submit(() -> {
                try {
                    Thread.sleep(SLEEP_TIME_MS); // Simulate blocking task
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Shutdown the executor and wait for tasks to finish
        virtualThreadPerTaskExecutor.shutdown();
        virtualThreadPerTaskExecutor.awaitTermination(10, TimeUnit.SECONDS);

        endTime = System.currentTimeMillis();
        result += "Virtual threads completed in " + (endTime - startTime) + " ms";
        return result;
    }

}
