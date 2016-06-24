package iotwechat.xlink.cloud.redis;

import iotwechat.xlink.cloud.config.RedisPool;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;

@Repository
public class RedisBase {

	@Autowired
	private RedisPool redisPool;

	/**
	 * 从服务器中取hashset值
	 * 
	 * @param key
	 * @param hashSet
	 * @return
	 */
	public String getHashSetValue(String key, String hashSet) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			value = jedis.hget(key, hashSet);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 设置hashset值
	 * 
	 * @param key
	 * @param filed
	 * @param value
	 */
	public void setHashSetValue(String key, String filed, String value) {
		// Jedis jedis = getJedis4Pool();
		// jedis.hset(key, filed, value);

		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			jedis.hset(key, filed, value);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
	}

	/**
	 * 从服务器中取hashset值
	 * 
	 * @param key
	 * @param hashSet
	 * @return
	 */
	public void setListValue(String key, String... strings) {
		// Jedis jedis = getJedis4Pool();
		// jedis.rpush(key, strings);
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			jedis.rpush(key, strings);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据Key 删除数据
	 * 
	 * @param key
	 */
	public void removeByKey(String key) {
		// Jedis jedis = getJedis4Pool();
		// jedis.del(key);
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
	}

	/**
	 * 
	 */
	public Map<String, String> hashGetAll(String key) {
		// Jedis jedis = getJedis4Pool();
		// return jedis.hgetAll(key);
		Map<String, String> value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			value = jedis.hgetAll(key);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
		return value;

	}

	public Long srem(String key, String... members) {
		// Jedis jedis = getJedis4Pool();
		// return jedis.srem(key,members);
		Long value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			value = jedis.srem(key, members);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
		return value;
	}

	public Set<String> smembers(String key) {
		// Jedis jedis = getJedis4Pool();
		// return jedis.smembers(key);
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			value = jedis.smembers(key);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
		return value;
	}

	private Jedis getJedis4Pool(int db) {

		Jedis jedis = redisPool.getJedis();
		if (jedis != null) {
			jedis.select(db);
		}

		return jedis;
	}

	public void sadd(String key, String members) {

		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			jedis.sadd(key, members);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}

	}

	/**
	 * 获取List数据
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> getListValue(String key, int start, int end) {

		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			value = jedis.lrange(key, start, end);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 删除一个key
	 * 
	 * @param key
	 */
	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(0);
			jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			redisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			redisPool.returnResource(jedis);
		}
	}

	/**
	 * 从指定db中拿取key
	 * 
	 * @param db
	 * @param key
	 */
	public String getValueFromDB(int db, String key) {
		String ret = null;
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(db);
			ret = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			if (jedis != null) {
				redisPool.returnBrokenResource(jedis);
			}
			e.printStackTrace();
		} finally {
			// 返还到连接池
			if (jedis != null) {
				redisPool.returnResource(jedis);
			}
		}

		return ret;
	}

	/**
	 * 向指定db中的key中写入一个值
	 * 
	 * @param db
	 * @param key
	 * @param value
	 */
	public void setValueToDB(int db, String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(db);
			jedis.set(key, value);
		} catch (Exception e) {
			// 释放redis对象
			if (jedis != null) {
				redisPool.returnBrokenResource(jedis);
			}
			e.printStackTrace();
		} finally {
			// 返还到连接池
			if (jedis != null) {
				redisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 设置DB中key的生存周期
	 * 
	 * @param db
	 * @param key
	 * @param sec
	 */
	public void setKeyExpireInDB(int db, String key, int sec) {
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(db);
			jedis.expire(key, sec);
		} catch (Exception e) {
			// 释放redis对象
			if (jedis != null) {
				redisPool.returnBrokenResource(jedis);
			}
			e.printStackTrace();
		} finally {
			// 返还到连接池
			if (jedis != null) {
				redisPool.returnResource(jedis);
			}
		}
	}

	/**
	 * 删除DB中key的生存周期
	 * 
	 * @param db
	 * @param key
	 */
	public void delKeyFromDB(int db, String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis4Pool(db);
			jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			if (jedis != null) {
				redisPool.returnBrokenResource(jedis);
			}
			e.printStackTrace();
		} finally {
			// 返还到连接池
			if (jedis != null) {
				redisPool.returnResource(jedis);
			}
		}
	}
}