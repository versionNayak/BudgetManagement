package com.finlabs.finexa.genericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.finlabs.finexa.model.CacheInfoDTO;
import com.finlabs.finexa.util.FinexaConstant;

@Component
public class CacheInfoService {
	@Autowired
	private RedisTemplate<String, CacheInfoDTO> redisTemplate;

	public List<?> getCacheList(String typeConstant, String tokenId, String subTypeConstant) {
		CacheInfoDTO cacheObject = (CacheInfoDTO) redisTemplate.opsForHash().get(typeConstant, tokenId);
		Map<String, List<?>> calculatedMethodListMap = null;
		List<?> unknownTypeList = null;
		if (cacheObject != null) {
			calculatedMethodListMap = cacheObject.getCalculatedMethodListMap();

			if (calculatedMethodListMap.get(subTypeConstant) != null) {
				unknownTypeList = calculatedMethodListMap.get(subTypeConstant);
			}
		}
		return unknownTypeList;
	}

	public void addCacheList(String typeConstant, String tokenId, String subTypeConstant, List<?> addList) {
		Map<String, List<?>> calculatedMethodListMap = new HashMap<>();
		CacheInfoDTO cacheObject = (CacheInfoDTO) redisTemplate.opsForHash().get(typeConstant, tokenId);

		if (cacheObject == null) {
			cacheObject = new CacheInfoDTO();
			calculatedMethodListMap = new HashMap<>();
		} else {
			calculatedMethodListMap = cacheObject.getCalculatedMethodListMap();
		}
		calculatedMethodListMap.put(subTypeConstant, addList);
		cacheObject.setCalculatedMethodListMap(calculatedMethodListMap);
		redisTemplate.opsForHash().put(typeConstant, tokenId, cacheObject);
		// redisTemplate.expire(typeConstant, 100000, TimeUnit.MILLISECONDS);
	}

	public Map<String, List<?>> getCacheMap(String typeConstant, String tokenId, String subTypeConstant) {
		CacheInfoDTO cacheObject = (CacheInfoDTO) redisTemplate.opsForHash().get(typeConstant, tokenId);
		Map<String, Map<String, List<?>>> calculatedMethodMap = null;
		Map<String, List<?>> unknownTypeMap = null;
		if (cacheObject != null) {
			calculatedMethodMap = cacheObject.getCalculatedMethodMap();

			if (calculatedMethodMap != null && calculatedMethodMap.get(subTypeConstant) != null) {
				unknownTypeMap = calculatedMethodMap.get(subTypeConstant);
			}
		}
		return unknownTypeMap;
	}

	public void addCacheMap(String typeConstant, String tokenId, String subTypeConstant, Map<String, List<?>> addMap) {
		Map<String, Map<String, List<?>>> calculatedMethodListMap = new HashMap<>();
		CacheInfoDTO cacheObject = (CacheInfoDTO) redisTemplate.opsForHash().get(typeConstant, tokenId);

		if (cacheObject == null) {
			cacheObject = new CacheInfoDTO();
			calculatedMethodListMap = new HashMap<>();
		} else {
			calculatedMethodListMap = cacheObject.getCalculatedMethodMap();
			if (calculatedMethodListMap == null) {
				calculatedMethodListMap = new HashMap<>();
			}
		}
		calculatedMethodListMap.put(subTypeConstant, addMap);
		cacheObject.setCalculatedMethodMap(calculatedMethodListMap);
		redisTemplate.opsForHash().put(typeConstant, tokenId, cacheObject);
		// redisTemplate.expire(typeConstant, 100000, TimeUnit.MILLISECONDS);
	}

	public String getToken(String header) {
		String token = "";
		if (header != null && header.startsWith(FinexaConstant.TOKEN_PREFIX))
			token = header.replace(FinexaConstant.TOKEN_PREFIX, "");
		return token;
	}
}
