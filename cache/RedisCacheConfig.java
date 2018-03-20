package me.ele.api.portal.configure.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ele.api.portal.service.redis.RedisCacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;

/**
* @author: hujunzheng
* @create: 17/9/29 下午1:56
*/
@Configuration
@EnableConfigurationProperties(RedisCacheProperties.class)
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisCacheProperties redisCacheProperties;

    /**
     * 构造jedis连接池配置对象
     *
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisCacheProperties.getMaxTotal());
        config.setMaxIdle(redisCacheProperties.getMaxIdle());
        config.setMinIdle(redisCacheProperties.getMinIdle());
        config.setMaxWaitMillis(redisCacheProperties.getMaxWaitMillis());
        config.setTestOnBorrow(redisCacheProperties.isTestOnBorrow());
        config.setTestWhileIdle(redisCacheProperties.isTestWhileIdle());
        return config;
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPool pool = new JedisPool(jedisPoolConfig(), redisCacheProperties.getHostName(), redisCacheProperties.getPort(), redisCacheProperties.getTimeout());
        return pool;
    }

    /**
     * 构造jedis连接工厂
     *
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisPoolConfig());
        factory.setHostName(redisCacheProperties.getHostName());
        factory.setPort(redisCacheProperties.getPort());
        //factory.setPassword(password);
        factory.setTimeout(redisCacheProperties.getTimeout());
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * 注入redis template
     *
     * @return
     */
    @Bean
    @Qualifier("redisTemplate")
    public RedisTemplate redisTemplate(@Autowired Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 注入redis cache manager
     *
     * @return
     */
    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(@Autowired @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(redisCacheProperties.getDefaultExpiration());
        return cacheManager;
    }

    @Bean
    public RedisCacheTemplate redisCacheTemplate() {
        return new RedisCacheTemplate();
    }

    @Bean
    public RedisCacheTemplate.RedisLockOperation redisLockOperation(@Autowired RedisCacheTemplate redisCacheTemplate) {
        return redisCacheTemplate.new RedisLockOperation();
    }

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisTemplate redisTemplate) {
        return new RedisLockRegistry(redisTemplate.getConnectionFactory(), "consumeStargateBuildMessage", 60000*2);
    }

    //第二种 继承了 CachingConfigurerSupport 的 cache策略
    /**
     * 配置缓存键值的生成器 (因为Redis是键值对存储的数据库, 所以要配置一个键值缓存的生成器)
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            // 配置生成器
            @Override
            public Object generate(Object target, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                // 规则1: 拼接class名称
                sb.append(target.getClass().getName());
                // 规则2: 拼接方法名称
                sb.append(method.getName());
                for (Object obj : objects) {
                    // 规则3: 拼接参数值
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 配置缓存时间, 单位秒
     * @param redisTemplate
     * @return
     */
    @Bean
    @Qualifier("cacheManagerWithStringRedisTemplate")
    public CacheManager cacheManager(@Autowired @Qualifier("stringRedisTemplate") StringRedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 设定缓存过期时间, 单位秒
        rcm.setDefaultExpiration(600);
        return rcm;
    }

    /**
     * @param factory
     * @return
     */
    @Bean
    @Qualifier("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(@Autowired @Qualifier("jedisConnectionFactory") RedisConnectionFactory factory,
        @Autowired Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置一个序列器, 将对象序列化为字符串存储, 和将对象反序列化为对象
     */
    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }
}
