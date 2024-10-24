package com.virtualthread.poc.server.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StatTestController {

    private static Logger logger = LoggerFactory.getLogger(StatTestController.class);


    @GetMapping("/block/{seconds}")
    public String blockingMethod(@PathVariable(name = "seconds") Integer seconds) throws InterruptedException {
        logger.error("~~~~~~ blockingMethod init ");
        Thread.sleep(1000L * seconds);
        logger.error("~~~~~~ blocked for {} seconds ", seconds);
        return Thread.currentThread().getName();
    }


}
