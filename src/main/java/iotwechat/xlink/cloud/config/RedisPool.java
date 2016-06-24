package iotwechat.xlink.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Repository
public class RedisPool {
	
	@Autowired
	private RedisConSettings redisConSettings;
	private static JedisPool pool = null;

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	private JedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			// config.setMaxActive(500);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			// config.setMaxIdle(5);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			// config.setMaxWait(1000 * 100);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			// pool = new JedisPool(config, redisConSettings.getHost(),
			// redisConSettings.getPort());
			pool = new JedisPool(config, redisConSettings.getHost(),
					redisConSettings.getPort(), Protocol.DEFAULT_TIMEOUT,
					redisConSettings.getPass(), redisConSettings.getDefaultdb());
		}
		return pool;
	}

	public void returnResource(Jedis redis) {
		if (redis != null) {
			getPool().returnResource(redis);
		}
	}

	public void returnBrokenResource(Jedis redis) {

		getPool().returnBrokenResource(redis);
	}

	public Jedis getJedis() {
		pool = getPool();
		Jedis jedis = pool.getResource();
		return jedis;

	}

}
