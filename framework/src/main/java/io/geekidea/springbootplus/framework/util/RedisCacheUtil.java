/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geekidea.springbootplus.framework.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yogaxu
 */
@Component
public class RedisCacheUtil {

    private static RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        RedisCacheUtil.redisTemplate = redisTemplate;
    }

    /**
     * 添加带<b>过期时长</b>的缓存
     *
     * @param key      键
     * @param value    内容
     * @param time     过期时长
     * @param timeUnit 时长单位
     */
    public static void set(final String key, final Object value, final long time, final TimeUnit timeUnit) {
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, (String) value, time, timeUnit);
        } else {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, timeUnit);
        }
    }

    /**
     * 添加带<b>过期时间</b>的缓存
     *
     * @param key     键
     * @param value   内容
     * @param timeout 过期时间
     */
    public static void set(final String key, final Object value, final Duration timeout) {
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, (String) value, timeout);
        } else {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value), timeout);
        }
    }

    /**
     * 获取<b>指定类型</b>缓存
     *
     * @param key   键
     * @param clazz 返回类型
     * @return T
     */
    public static <T> T get(final String key, final Class<T> clazz) {
        return JSON.parseObject(redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 获取<b>字符串</b>缓存
     *
     * @param key 键
     * @return String
     */
    public static String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取key集
     *
     * @param pattern 标识
     * @return Set<String>
     */
    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取多个缓存
     *
     * @param keys 键集
     * @return List<String>
     */
    public static List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除<b>单个key</b>缓存
     *
     * @param key 键
     * @return Boolean
     */
    public static Boolean delete(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除<b>多个key</b>缓存
     *
     * @param keys 键集
     * @return Long
     */
    public static Long delete(final Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

}
