package com.sparta.msa_exam.product.config;

import static org.springframework.data.redis.serializer.RedisSerializationContext.*;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {
	@Bean
	// CacheManager로 진행해도 정상 동작
	//cacheManger 기본값 설정
	public RedisCacheManager cacheManager(
		RedisConnectionFactory redisConnectionFactory
	) {
		// 설정 구성을 먼저 진행한다.
		// Redis를 이용해서 Spring Cache를 사용할 때
		// Redis 관련 설정을 모아두는 클래스
		RedisCacheConfiguration configuration = RedisCacheConfiguration
			.defaultCacheConfig()
			// null을 캐싱 할것인지
			.disableCachingNullValues()
			// 기본 캐시 유지 시간 (Time To Live)
			.entryTtl(Duration.ofSeconds(60))
			// 캐시를 구분하는 접두사 설정
			.computePrefixWith(CacheKeyPrefix.simple())
			// 캐시에 저장할 값(value)을 어떻게 직렬화 / 역직렬화 할것인지(java,json,string...)
			.serializeValuesWith(
				SerializationPair.fromSerializer(RedisSerializer.java())
			);

		return RedisCacheManager
			.builder(redisConnectionFactory)
			.cacheDefaults(configuration)
			.build();
	}
}
