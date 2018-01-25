package com.newtouch.common.view;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description JSON 串 到 OBJ的转换工具
 * @author c_houyongfeng
 * @version 1.0
 */
public class JsonUtils {
	private static ObjectMapper mapper = new ObjectMapper();
	public static String toJson(Object object) {

		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new RuntimeException("object to json error!", e);
		}
		return jsonString;
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("render json to object error!", e);
		}
	}

	public static <T> List<T> toObjectList(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, new TypeReference<List<T>>() { });
		} catch (Exception e) {
			throw new RuntimeException("from Json发生错误", e);
		}
	}
}
