package com.indoqa.daisy.caching;

import org.apache.cocoon.configuration.Settings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CachingAspect {

    private static final Logger logger = LoggerFactory.getLogger(CachingAspect.class);
    private static final int DEFAULT_EXPIRY_TIME_IN_MINUTES = 10;

    private static int expires = 0;

    @Autowired
    private Cache cache;

    @Autowired
    private CacheRefreshManager cacheRefreshManager;

    @Autowired
    private Settings settings;

    @Around("execution(* com.indoqa.daisy.dao.BinaryDocumentDao.*(..))")
    public Object cacheBinaryDocuments(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.doCaching(joinPoint);
    }

    @Around("execution(* com.indoqa.daisy.dao.ContentDocumentDao.*(..))")
    public Object cacheContentDocuments(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.doCaching(joinPoint);
    }

    @Around("execution(* com.indoqa.daisy.dao.NavigationDao.*(..))")
    public Object cacheNavigationDocuments(ProceedingJoinPoint joinPoint) throws Throwable {
        return this.doCaching(joinPoint);
    }

    protected Result putJointPointResultIntoCache(ProceedingJoinPoint joinPoint, String cacheKey) throws Throwable {
        Result result = new Result(joinPoint.proceed(), System.currentTimeMillis() + this.readExpiryTime() * 60 * 1000);

        this.cache.put(cacheKey, result);
        if (logger.isDebugEnabled()) {
            logger.debug("Loading: " + cacheKey);
        }

        return result;
    }

    private String createCacheKey(ProceedingJoinPoint joinPoint) {
        StringBuilder cacheKey = new StringBuilder();

        cacheKey.append(joinPoint.getTarget().getClass().getName());
        cacheKey.append("#");
        cacheKey.append(joinPoint.getSignature().getName());
        cacheKey.append("(");

        Object[] args = joinPoint.getArgs();
        int counter = 0;
        for (Object object : args) {
            if (counter++ > 0) {
                cacheKey.append(", ");
            }

            if (object == null) {
                cacheKey.append("null");
            } else {
                cacheKey.append("'");
                cacheKey.append(object.toString());
                cacheKey.append("'");
            }

        }
        cacheKey.append(")");

        return cacheKey.toString();
    }

    private Object doCaching(ProceedingJoinPoint joinPoint) throws Throwable {
        String cacheKey = this.createCacheKey(joinPoint);
        Result result = this.cache.get(cacheKey);

        // if there is no result in cache or caching is disabled, load from CMS. Otherwise cache
        // asynchronously.
        if (!this.isAsyncCachingEnabled()) {
            result = this.putJointPointResultIntoCache(joinPoint, cacheKey);
        } else if (result == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No results in cache. Reading from CMS: " + cacheKey);
            }

            result = this.putJointPointResultIntoCache(joinPoint, cacheKey);
        } else if (result.getValidUntil() < System.currentTimeMillis()) {
            logger.debug("Result in cache is outdated since " + (System.currentTimeMillis() - result.getValidUntil())
                    + "ms. Reading from CMS: " + cacheKey);

            this.cacheRefreshManager.refresh(joinPoint, cacheKey, this);
        }

        return result.getValue();
    }

    private boolean isAsyncCachingEnabled() {
        return Boolean.parseBoolean(this.settings.getProperty("com.indoqa.daisy.caching.async"));
    }

    private int readExpiryTime() {
        if (expires <= 0) {
            String expiryTimeInMinutes = this.settings.getProperty("com.indoqa.daisy.caching.expires");

            if (StringUtils.isNotBlank(expiryTimeInMinutes) && NumberUtils.isDigits(expiryTimeInMinutes)) {
                expires = Integer.parseInt(expiryTimeInMinutes);
            } else {
                expires = DEFAULT_EXPIRY_TIME_IN_MINUTES;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Setting expiry time to " + expires + " minutes.");
            }
        }

        return expires;
    }
}
