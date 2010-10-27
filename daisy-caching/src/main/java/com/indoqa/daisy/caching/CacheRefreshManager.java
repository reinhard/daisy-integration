package com.indoqa.daisy.caching;

import org.aspectj.lang.ProceedingJoinPoint;

public interface CacheRefreshManager {

    void refresh(ProceedingJoinPoint joinPoint, String cacheKey, CachingAspect cachingAspect);
}
