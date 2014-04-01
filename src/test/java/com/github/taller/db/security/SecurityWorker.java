package com.github.taller.db.security;

import com.github.taller.db.security.beans.ObjectType;
import com.github.taller.db.security.beans.Operation;
import com.github.taller.db.security.beans.OperationProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Author: Ivan A. Ivanchikov (taller@github.com)
 * Date: 01.04.14
 */
public class SecurityWorker implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(SecurityWorker.class);
    private SecurityCache cache;
    private int maxRepeat;

    public SecurityWorker(SecurityCache cache, int maxRepeat) {
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
            int tableSfx = r.nextInt(5) + 1;

            boolean result = cache.allowed("auth" + authSfx, "key" + keySfx, "public.table" + tableSfx + authSfx,
                    ObjectType.TABLE, Operation.READ);
            log.debug("auth" + authSfx + "\nkey" + keySfx + "\npublic.table" + tableSfx + authSfx);
            log.info("Result = {} on repeat = {}", result, c);
            if (!result && authSfx == keySfx) {
                log.error("auth{} | key{}", authSfx, keySfx);
            } else {
                log.info("auth{} | key{}", authSfx, keySfx);
            }
            c++;
        }
        log.info("Finished - {}", c);
    }}
