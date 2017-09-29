import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author: hujunzheng
* @create: 17/9/29 下午2:49
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {
    /**
     * redis的key
     * @return
     */
    String value();
    /**
     * 持锁时间,单位毫秒,默认一分钟
     */
    long keepMills() default 60000;
    /**
     * 当获取失败时候动作
     */
    LockFailAction action() default LockFailAction.GIVEUP;
    
    public enum LockFailAction{
        /**
         * 放弃
         */
        GIVEUP,
        /**
         * 继续
         */
        CONTINUE;
    }
    /**
     * 睡眠时间,设置GIVEUP忽略此项
     * @return
     */
    long sleepMills() default 500;
}
