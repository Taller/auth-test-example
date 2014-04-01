package com.github.taller.db.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 30.03.14
 */
public class AuthWorker implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(AuthWorker.class);
    private AuthCache cache;
    private int maxRepeat;

    public AuthWorker(AuthCache cache, int maxRepeat) {
        this.cache = cache;
        this.maxRepeat = maxRepeat;

    }

    @Override
    public void run() {
        if (cache == null) {
            return;
        }
        int c = 0;
        while (c < maxRepeat) {
            Random r = new Random();
            int authSfx = r.nextInt(3) + 1;
            int keySfx = r.nextInt(2) + 1;

            boolean result = cache.allowed("auth" + authSfx, "key" + keySfx);

            log.info("Result = {} on repeat = {}", result, c);
            if (!result && authSfx == keySfx) {
                log.error("auth{} | key{}", authSfx, keySfx);
            } else {
                log.info("auth{} | key{}", authSfx, keySfx);
            }
            c++;
        }
        log.info("Finished - {}", c);
    }
}
