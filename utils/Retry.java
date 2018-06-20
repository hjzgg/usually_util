import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class Retry {
    private static final long DEFAULT_BACK_OFF_PERIOD = 1000L;
    private static final int DEFAULT_MAX_ATTEMPTS = 2;

    private RetryTemplate retryTemplate = new RetryTemplate();

    public Retry() {
        this(DEFAULT_MAX_ATTEMPTS, DEFAULT_BACK_OFF_PERIOD);
    }

    /**
    * @param maxAttempts 最大尝试次数
    * */
    public Retry(int maxAttempts) {
        this(maxAttempts, DEFAULT_BACK_OFF_PERIOD);
    }

    /**
     * @param maxAttempts 最大尝试次数
     * @param backOffPeriod 尝试周期，单位：ms
     * */
    public Retry(int maxAttempts, long backOffPeriod) {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backOffPeriod);
        this.retryTemplate.setRetryPolicy(retryPolicy);
        this.retryTemplate.setBackOffPolicy(backOffPolicy);
    }

    public <T, E extends Exception> T execute(RetryCallback<T, E> retryCallback) throws E {
        return retryTemplate.execute(retryCallback);
    }

    public <T, E extends Exception> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback) throws E {
        return retryTemplate.execute(retryCallback, recoveryCallback);
    }
}
