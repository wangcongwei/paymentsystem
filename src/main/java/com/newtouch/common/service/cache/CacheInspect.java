package com.newtouch.common.service.cache;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.newtouch.common.annotation.cache.Cacheable;

/**
 * 拦截PutCache注解进行缓存保存
 * 
 * @author dongfeng.zhang
 */
@Service
public class CacheInspect {

	private static final String CACHE_NAME = "$$ALL_NAMES$$";
	private static final String CACHE_KEY_SUF = ".$$ALL_KEY$$";
	private static final String CACHE_NAME_KEY_PRE = "[";
	private static final String CACHE_NAME_KEY_SUF = "]";

	@Autowired
	private CacheManager cacheManager;

	/**
	 * 获取某个缓存大类下面的所有缓存类目名称
	 * 
	 * @param catalog
	 * @return
	 */
	public Set<String> getCachedNames(Cacheable.Catalog catalog) {
		Cache cache = getCache(catalog.getCode());
		if (cache == null) {
			return null;
		}
		return (Set<String>) getValue(cache, CACHE_NAME);
	}

	/**
	 * 获取某个缓存类目下面所有缓存条目名称
	 * 
	 * @param catalog
	 * @param name
	 * @return
	 */
	public Set<String> getCachedKeys(Cacheable.Catalog catalog, String name) {
		Cache cache = getCache(catalog.getCode());
		if (cache == null) {
			return null;
		}
		return (Set<String>) getValue(cache, name + CACHE_KEY_SUF);
	}

	/**
	 * 添加到缓存
	 * 
	 * @param catalog
	 * @param name
	 * @param key
	 * @param object
	 */
	public void putCachedData(Cacheable.Catalog catalog, String name, String key, Object object) {
		Cache cache = getCache(catalog.getCode());
		Set<String> names = (Set<String>) getValue(cache, CACHE_NAME);
		Set<String> keys = (Set<String>) getValue(cache, name + CACHE_KEY_SUF);

		if (names == null) {
			names = new HashSet<String>(0);
		}
		names.add(name);

		if (keys == null) {
			keys = new HashSet<String>(0);
		}
		keys.add(key);

		cache.put(CACHE_NAME, names);// put name
		cache.put(name + CACHE_KEY_SUF, keys);// put key
		cache.put(mergeKey(name, key), object);// put value
	}

	/**
	 * 获取缓存值
	 * 
	 * @param catalog
	 * @param name
	 * @param key
	 * @return
	 */
	public Object getCachedData(Cacheable.Catalog catalog, String name, String key) {
		Cache cache = getCache(catalog.getCode());
		return getValue(cache, mergeKey(name, key));
	}

	/**
	 * 从缓存移除
	 * 
	 * @param catalog
	 * @param name
	 * @param key
	 */
	public void removeCachedData(Cacheable.Catalog catalog, String name, String key) {
		Cache cache = getCache(catalog.getCode());
		Set<String> names = (Set<String>) getValue(cache, CACHE_NAME);
		Set<String> keys = (Set<String>) getValue(cache, name + CACHE_KEY_SUF);

		if (keys == null) {
			keys = new HashSet<String>(0);
		} else if (keys.contains(key)) {
			keys.remove(key);
		}

		if (names == null) {
			names = new HashSet<String>(0);
		} else if (names.contains(name)) {
			if (keys.isEmpty()) {
				names.remove(name);
			}
		}

		cache.put(CACHE_NAME, names);// reput names
		cache.put(name + CACHE_KEY_SUF, keys);// reput keys
		cache.evict(mergeKey(name, key));
	}

	/**
	 * 移除类目下所有缓存条目
	 * 
	 * @param catalog
	 * @param name
	 */
	public void removeAllCachedData(Cacheable.Catalog catalog, String name) {
		Cache cache = getCache(catalog.getCode());
		Set<String> keys = (Set<String>) getValue(cache, name + CACHE_KEY_SUF);
		if (keys != null) {
			for (String key : keys) {
				removeCachedData(catalog, name, key);
			}
		}
	}

	/**
	 * 清空缓存大类
	 * 
	 * @param catalog
	 */
	public void removeALLCacheData(Cacheable.Catalog catalog) {
		Cache cache = getCache(catalog.getCode());
		cache.clear();
	}

	public void removeALLCacheData() {
		removeALLCacheData(Cacheable.Catalog.CDCache);
		removeALLCacheData(Cacheable.Catalog.DDCache);
		removeALLCacheData(Cacheable.Catalog.IDCache);
	}

	/**
	 * 获取最终存储key
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	private String mergeKey(String name, String key) {
		return name + CACHE_NAME_KEY_PRE + key + CACHE_NAME_KEY_SUF;
	}

	/**
	 * 
	 * @param cacheName
	 * @return
	 */
	private Cache getCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		Assert.notNull(cache, "cache[" + cacheName + "] not found!");
		return cache;
	}

	private Object getValue(Cache cache, String key) {
		Assert.notNull(key, "cache key [" + key + "] not found!");
		ValueWrapper vw = cache.get(key);
		if (vw == null) {
			return null;
		}
		return vw.get();
	}
}
