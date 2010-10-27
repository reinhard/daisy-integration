package com.indoqa.daisy.caching;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheRefreshManagerImpl implements CacheRefreshManager {

    private static final int threadPoolSize = 50;

    private final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

    private static final Logger logger = LoggerFactory.getLogger(CacheRefreshManagerImpl.class);

    private final List<String> pendingCacheKeys = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void refresh(ProceedingJoinPoint joinPoint, String cacheKey, CachingAspect cachingAspect) {
        if (logger.isDebugEnabled()) {
            logger.debug("Register join point for reloading: " + cacheKey);
        }

        if (this.pendingCacheKeys.contains(cacheKey)) {
            // the refresh of this cache key is already scheduled
            if (logger.isDebugEnabled()) {
                logger.debug("Refreshing of this cache key is already scheduled: " + cacheKey);
            }

            return;
        }

        this.pendingCacheKeys.add(cacheKey);
        this.executorService.execute(new RefreshWorker(joinPoint, cacheKey, cachingAspect));
    }

    protected void executeCacheRefreshJob(ProceedingJoinPoint joinPoint, String cacheKey, CachingAspect cachingAspect) {
        if (logger.isDebugEnabled()) {
            logger.debug("Execute cache refresh job: " + cacheKey);
        }

        try {
            cachingAspect.putJointPointResultIntoCache(joinPoint, cacheKey);
        } catch (Throwable t) {
            logger.error("Can't proceed join point.", t);
        } finally {
            this.pendingCacheKeys.remove(cacheKey);

            if (logger.isDebugEnabled()) {
                logger.debug("Remove scheduled cache key: " + cacheKey);
            }
        }
    }

    private class RefreshWorker implements Runnable {

        private final ProceedingJoinPoint joinPoint;
        private final String cacheKey;
        private final CachingAspect cachingAspect;

        public RefreshWorker(ProceedingJoinPoint joinPoint, String cacheKey, CachingAspect cachingAspect) {
            this.joinPoint = joinPoint;
            this.cacheKey = cacheKey;
            this.cachingAspect = cachingAspect;
        }

        public void run() {
            CacheRefreshManagerImpl.this.executeCacheRefreshJob(this.joinPoint, this.cacheKey, this.cachingAspect);
        }
    }
}
