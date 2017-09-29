import java.lang.reflect.Method;

import me.ele.elog.Log;
import me.ele.elog.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* @author: hujunzheng
* @create: 17/9/29 下午2:49
*/
@Component
@Aspect
public class RedisLockAspect {
    private static final Log log = LogFactory.getLog(RedisLockAspect.class);

    @Autowired
    private RedisCacheTemplate.RedisLockOperation redisLockOperation;

    @Pointcut("execution(* me.ele..StargateDeployMessageConsumer.consumeStargateDeployMessage(..))" +
            "&& @annotation(me.ele.api.portal.service.redis.RedisLock)")
    private void lockPoint(){}

    @Around("lockPoint()")
    public Object arround(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        RedisLock lockInfo = method.getAnnotation(RedisLock.class);
        
        boolean lock = false;
        Object obj = null;
        while(!lock){
            long timestamp = System.currentTimeMillis()+lockInfo.keepMills();
            lock = setNX(lockInfo.value(), timestamp);
            //得到锁，已过期并且成功设置后旧的时间戳依然是过期的，可以认为获取到了锁(成功设置防止锁竞争)
            long now = System.currentTimeMillis();
            if(lock || ((now > getLock(lockInfo.value())) && (now > getSet(lockInfo.value(), timestamp)))){
                log.info("得到redis分布式锁...");
                obj = pjp.proceed();
                if(lockInfo.action().equals(RedisLock.LockFailAction.CONTINUE)){
                    releaseLock(lockInfo.value());
                }
            }else{
                if(lockInfo.action().equals(RedisLock.LockFailAction.CONTINUE)){
                    log.info("稍后重新请求redis分布式锁...");
                    Thread.currentThread().sleep(lockInfo.sleepMills());
                }else{
                    log.info("放弃redis分布式锁...");
                    break;
                }
            }
        }
        return obj;
    }
    private boolean setNX(String key,Long value){
        return redisLockOperation.setNX(key, value);
    }
    private long getLock(String key){
        return redisLockOperation.get(key);
    }
    private Long getSet(String key,Long value){
        return redisLockOperation.getSet(key, value);
    }
    private void releaseLock(String key){
        redisLockOperation.delete(key);
    }
}
