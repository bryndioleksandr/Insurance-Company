package org.company.insurance.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class MultiLevelCacheResolver implements CacheResolver {
    private final CacheManager localCacheManager;

    public MultiLevelCacheResolver(CacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        return Stream.of(
                        localCacheManager.getCache("auto-insurances"),
                        localCacheManager.getCache("travel-insurances"),
                        localCacheManager.getCache("claims"),
                        localCacheManager.getCache("claim-assessments"),
                        localCacheManager.getCache("health-insurances"),
                        localCacheManager.getCache("insurance-policies"),
                        localCacheManager.getCache("policy-holders"),
                        localCacheManager.getCache("property-insurances"),
                        localCacheManager.getCache("users")
                )
                .filter(Objects::nonNull)
                .peek(cache -> {
                    if (cache == null) {
                        log.warn("Cache not found in CacheManager for context: {}", context.getOperation().getCacheNames());
                    }
                })
                .toList();
    }

}