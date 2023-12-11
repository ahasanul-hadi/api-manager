package com.bnda.apim.filter;

import com.bnda.apim.api.Subscription;
import com.bnda.apim.util.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;

@Component
@Slf4j
@Primary
public class CustomRateLimiter implements RateLimiter {

    private final RedisRateLimiter rateLimiter;
    private final RedisScript<List<Long>> script;
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final DataRepository dataRepository;

    public CustomRateLimiter(
            RedisRateLimiter rateLimiter,
            @Qualifier(RedisRateLimiter.REDIS_SCRIPT_NAME) RedisScript<List<Long>> script,
            ReactiveRedisTemplate<String, String> redisTemplate, DataRepository dataRepository) {

        this.rateLimiter = rateLimiter;
        this.script = script;
        this.redisTemplate = redisTemplate;
        this.dataRepository= dataRepository;
    }

    // These two methods are the core of the rate limiter
    // Their purpose is to come up with a rate limits for given API KEY (or user ID)
    // It is up to implementor to return limits based up on the api key passed
    private int getBurstCapacity(String routeId, String apiKey) {
        long key= Long.parseLong(apiKey);
        Optional<Subscription> subOptional=  dataRepository.getSubscriptionById(key);
        return subOptional.map(Subscription::getRequestPerSecLimit).orElse(1);
    }
    private int getReplenishRate(String routeId, String apiKey) {
        long key= Long.parseLong(apiKey);
        Optional<Subscription> subOptional=  dataRepository.getSubscriptionById(key);
        return subOptional.map(Subscription::getRequestPerSecLimit).orElse(1);
    }

    public Mono<Response> isAllowed(String routeId, String apiKey) {

        int replenishRate = getReplenishRate(routeId, apiKey);
        int burstCapacity = getBurstCapacity(routeId, apiKey);

        log.debug("replenishRate:"+replenishRate+" burstCapacity:"+burstCapacity);

        try {
            List<String> keys = getKeys(apiKey);

            // The arguments to the LUA script. time() returns unixtime in seconds.
            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "",
                    Instant.now().getEpochSecond() + "", "1");
            Flux<List<Long>> flux = this.redisTemplate.execute(this.script, keys, scriptArgs);

            return flux.onErrorResume(throwable -> {
                        log.error("error:"+throwable);
                        return Flux.just(Arrays.asList(-1L, -1L));
                    })
                    .reduce(new ArrayList(), (longs, l) -> {
                        longs.addAll(l);
                        return longs;
                    }) .map(results -> {
                        log.info("results.get(0):"+(long)results.get(0)+" results.get(1):"+results.get(1));

                        boolean allowed = (long)results.get(0) == 1L;
                        Long tokensLeft = (long)results.get(1);

                        Response response = new Response(allowed, getHeaders(tokensLeft, replenishRate, burstCapacity));
                        log.debug("response: " + response);
                        return response;
                    });
        }
        catch (Exception e) {
            /*
             * We don't want a hard dependency on Redis to allow traffic. Make sure to set
             * an alert so you know if this is happening too much. Stripe's observed
             * failure rate is 0.01%.
             */
            log.error("Error determining if user allowed from redis", e);
        }
        return Mono.just(new Response(true, getHeaders(-1L, replenishRate, burstCapacity)));
    }

    private static List<String> getKeys(String id) {
        String prefix = "request_rate_limiter.{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    private HashMap<String, String> getHeaders(Long tokensLeft, Integer replenish, Integer burst) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(RedisRateLimiter.REMAINING_HEADER, tokensLeft.toString());
        headers.put(RedisRateLimiter.REPLENISH_RATE_HEADER, replenish.toString());
        headers.put(RedisRateLimiter.BURST_CAPACITY_HEADER, burst.toString());
        return headers;
    }

    @Override
    public Map<String, RedisRateLimiter.Config> getConfig() {
        return rateLimiter.getConfig();
    }

    @Override
    public Class getConfigClass() {
        return rateLimiter.getConfigClass();
    }

    @Override
    public Object newConfig() {
        return rateLimiter.newConfig();
    }
}