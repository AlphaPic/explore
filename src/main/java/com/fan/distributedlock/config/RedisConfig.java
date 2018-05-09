package com.fan.distributedlock.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author:fanwenlong
 * @date:2018-05-08 16:56:11
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component
public class RedisConfig {

    Integer REDIS_MAXIDLE      = 100;      /** 最大的空闲数 */
    Integer REDIS_MAXACTIVE    = 100;      /** 最大的活跃数 */
    Integer REDIS_MAXWAIT      = 1000;     /** 最长等待时间 */
    Boolean REDIS_TESTONBORROW = true;     /** 未知 */

    String  REDIS_HOST1     = "10.112.2.88";       /** redis主机ip1 */
    String  REDIS_HOST2     = "10.112.2.63";       /** redis主机ip2 */
    String  REDIS_HOST3     = "10.112.2.140";      /** redis主机ip3 */
    Integer REDIS_PORT1     = 7000;                /** redis端口1 */
    Integer REDIS_PORT2     = 7001;                /** redis端口2 */
    Integer REDIS_PORT3     = 7002;                /** redis端口3 */
    String  REDIS_AUTH      = null;                /** 密码 */
    Integer REDIS_TIMEOUT   = 100;                 /** 超时时间 */

    @Bean
    public JedisPoolConfig getPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(REDIS_MAXIDLE);
        config.setTestOnBorrow(REDIS_TESTONBORROW);
        config.setMaxWaitMillis(REDIS_MAXWAIT);
        return config;
    }

    @Bean(name = "node11")
    public JedisConnectionFactory getRedisConnectionFactory11(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST1,REDIS_PORT1,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }

    @Bean(name = "node12")
    public JedisConnectionFactory getRedisConnectionFactory12(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST1,REDIS_PORT2,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }

    @Bean(name = "node13")
    public JedisConnectionFactory getRedisConnectionFactory13(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST1,REDIS_PORT3,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }

    @Bean(name = "node21")
    public JedisConnectionFactory getRedisConnectionFactory21(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST2,REDIS_PORT1,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }

    @Bean(name = "node22")
    public JedisConnectionFactory getRedisConnectionFactory22(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST2,REDIS_PORT2,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }

    @Bean(name = "node23")
    public JedisConnectionFactory getRedisConnectionFactory23(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST2,REDIS_PORT3,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }


    @Bean(name = "node31")
    public JedisConnectionFactory getRedisConnectionFactory31(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST3,REDIS_PORT1,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }


    @Bean(name = "node32")
    public JedisConnectionFactory getRedisConnectionFactory32(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST3,REDIS_PORT2,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }


    @Bean(name = "node33")
    public JedisConnectionFactory getRedisConnectionFactory33(JedisPoolConfig config){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        setFactoryInfo(factory,REDIS_HOST3,REDIS_PORT3,REDIS_TIMEOUT,REDIS_AUTH);
        return factory;
    }

    private void setFactoryInfo(JedisConnectionFactory factory,String host,Integer port,Integer timeout,String auth){
        factory.setHostName(host);
        factory.setPort(port);
        factory.setTimeout(timeout);
        factory.setPassword(auth);
        factory.afterPropertiesSet();
    }

    /** key-value的类型为String-String类型的模板 */
    @Bean(name = "template11")
    public RedisTemplate<String,String> getStringRedisTemplate11(@Qualifier("node11") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template12")
    public RedisTemplate<String,String> getStringRedisTemplate12(@Qualifier("node12") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template13")
    public RedisTemplate<String,String> getStringRedisTemplate13(@Qualifier("node13") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template21")
    public RedisTemplate<String,String> getStringRedisTemplate21(@Qualifier("node21") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template22")
    public RedisTemplate<String,String> getStringRedisTemplate22(@Qualifier("node22") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template23")
    public RedisTemplate<String,String> getStringRedisTemplate23(@Qualifier("node23") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template31")
    public RedisTemplate<String,String> getStringRedisTemplate31(@Qualifier("node31") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template32")
    public RedisTemplate<String,String> getStringRedisTemplate32(@Qualifier("node32") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }
    @Bean(name = "template33")
    public RedisTemplate<String,String> getStringRedisTemplate33(@Qualifier("node33") RedisConnectionFactory cf){
        return getStringRedisTemplate(cf);
    }

    private RedisTemplate<String,String> getStringRedisTemplate(RedisConnectionFactory cf){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<String,String>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    /** key-value的类型为String-Object类型的模板 */
//    @Bean(name = "objectRedisTemplate")
//    public RedisTemplate<String,Object> getIntegerRedisTemplate(RedisConnectionFactory cf){
//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
//        redisTemplate.setConnectionFactory(cf);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}
