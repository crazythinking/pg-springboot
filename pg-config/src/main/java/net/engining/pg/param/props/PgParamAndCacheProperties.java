package net.engining.pg.param.props;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 工程通用配置
 * 
 * @author luxue
 *
 */
@ConfigurationProperties(prefix = "pg.param")
public class PgParamAndCacheProperties {

	/**
	 * 是否使用Json ParameterFacility
	 */
	private boolean jsonParameterFacility = false;

	/**
	 * 缓存过期时间值
	 */
	private long expireDuration = 5;

	/**
	 * 缓存过期时间单位
	 */
	private TimeUnit expireTimeUnit = TimeUnit.MINUTES;

	/**
	 * 是否使用RedisCache的开关, 默认false
	 */
	private boolean enableRedisCache = false;

	/**
	 * @return the jsonParameterFacility
	 */
	public boolean isJsonParameterFacility() {
		return jsonParameterFacility;
	}

	/**
	 * @param jsonParameterFacility the jsonParameterFacility to set
	 */
	public void setJsonParameterFacility(boolean jsonParameterFacility) {
		this.jsonParameterFacility = jsonParameterFacility;
	}

	/**
	 * @return the expireDuration
	 */
	public long getExpireDuration() {
		return expireDuration;
	}

	/**
	 * @param expireDuration the expireDuration to set
	 */
	public void setExpireDuration(long expireDuration) {
		this.expireDuration = expireDuration;
	}

	/**
	 * @return the expireTimeUnit
	 */
	public TimeUnit getExpireTimeUnit() {
		return expireTimeUnit;
	}

	/**
	 * @param expireTimeUnit the expireTimeUnit to set
	 */
	public void setExpireTimeUnit(TimeUnit expireTimeUnit) {
		this.expireTimeUnit = expireTimeUnit;
	}

	/**
	 * @return the enableRedisCache
	 */
	public boolean isEnableRedisCache() {
		return enableRedisCache;
	}

	/**
	 * @param enableRedisCache the enableRedisCache to set
	 */
	public void setEnableRedisCache(boolean enableRedisCache) {
		this.enableRedisCache = enableRedisCache;
	}

}
