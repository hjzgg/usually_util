import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hujunzheng hujunzheng
 * @create 2017-09-29 下午2:58
 **/
@ConfigurationProperties(prefix = "apiportal.redis")
public class RedisCacheProperties {
    @Value("${pool.maxTotal:10}")
    private int maxTotal; // 最大连接数
    @Value("${pool.maxIdle:10}")
    private int maxIdle; // 最大空闲连接数
    @Value("${pool.minIdle:0}")
    private int minIdle; // 最小空闲连接数
    @Value("${pool.maxWaitMillis:-1}")
    private long maxWaitMillis; // 最大建立连接等待时间
    @Value("${pool.testOnBorrow:false}")
    private boolean testOnBorrow; // 获取连接时检查有效性
    @Value("${pool.testWhileIdle:false}")
    private boolean testWhileIdle; // 空闲时检查有效性

    @Value("${hostName:127.0.0.1}")
    private String hostName; // 主机名
    @Value("${port:6379}")
    private int port; // 监听端口
    @Value("${password:}")
    private String password; // 密码
    @Value("${timeout:2000}")
    private int timeout; // 客户端连接时的超时时间（单位为秒）

    @Value("${cache.defaultExpiration:3600}")
    private long defaultExpiration; // 缓存时间，单位为秒（默认为0，表示永不过期）

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }
}
