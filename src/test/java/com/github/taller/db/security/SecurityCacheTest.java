package com.github.taller.db.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 30.03.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-test.xml")
public class SecurityCacheTest {
    @Autowired
    SecurityCache cache;


    @Test
    public void test() {
        ExecutorService executor = Executors.newFixedThreadPool(150);
        for (int i = 0; i < 1000; i++) {
            Runnable worker = new SecurityWorker(cache, 1000000);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
