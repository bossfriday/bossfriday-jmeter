package cn.bossfriday.jmeter.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * ThreadPoolUtils
 *
 * @author chenx
 */
public class ThreadPoolUtils {

    public static final int AVAILABLE_PROCESSORS;

    private static final ConcurrentHashMap<String, ExecutorService> THREAD_MAP = new ConcurrentHashMap<>();
    private static final String STRING_PLACEHOLDER = "%d";

    private ThreadPoolUtils() {

    }

    static {
        AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    }

    /**
     * getSingleThreadExecutor
     *
     * @param name
     * @return
     */
    public static ExecutorService getSingleThreadExecutor(String name) {
        return getThreadPool(name, name, 1, 1, 0, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * getSingleThreadExecutor
     *
     * @param name
     * @param workerQueueSize
     * @return
     */
    public static ExecutorService getSingleThreadExecutor(String name, int workerQueueSize) {
        return getThreadPool(name, name, 1, 1, workerQueueSize, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * getThreadPool
     *
     * @param name
     * @return
     */
    public static ExecutorService getThreadPool(String name) {
        return getThreadPool(name, ThreadPoolUtils.AVAILABLE_PROCESSORS);
    }

    /**
     * getThreadPool
     *
     * @param name
     * @param size
     * @return
     */
    public static ExecutorService getThreadPool(String name, int size) {
        return getThreadPool(name, name, size);
    }

    /**
     * getThreadPool
     *
     * @param name
     * @param threadNamePrefix
     * @param coreSize
     * @return
     */
    public static ExecutorService getThreadPool(String name,
                                                String threadNamePrefix,
                                                int coreSize) {
        return getThreadPool(name, threadNamePrefix, coreSize, 0);
    }

    /**
     * getThreadPool
     *
     * @param name
     * @param threadNamePrefix
     * @param coreSize
     * @param workerQueueSize
     * @return
     */
    public static ExecutorService getThreadPool(String name,
                                                String threadNamePrefix,
                                                int coreSize,
                                                int workerQueueSize) {
        return getThreadPool(name, threadNamePrefix, coreSize, coreSize * 2, workerQueueSize, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * @param name             线程池名称,
     * @param threadNamePrefix 线程名称前缀.
     * @param coreSize         线程数量. 必须> 1
     * @param maxThreadSize    最大数量. 必须> 1 ,并且大于 coreSize ,否则使用coreSize
     * @param workerQueueSize  线程队列数量, 当 workerQueueSize <=0  workerQueueSize:使用默认值 Integer.MAX_VALUE
     * @param rejectedHandler  拒绝策略, 如果为空 使用 ThreadPoolExecutor.AbortPolicy()
     */
    public static ExecutorService getThreadPool(String name,
                                                String threadNamePrefix,
                                                int coreSize,
                                                int maxThreadSize,
                                                int workerQueueSize,
                                                RejectedExecutionHandler rejectedHandler) {
        if (!THREAD_MAP.containsKey(name)) {
            ExecutorService pool = new ThreadPoolExecutor(
                    coreSize,
                    maxThreadSize > coreSize ? maxThreadSize : coreSize, 0L,
                    TimeUnit.MILLISECONDS,
                    getWorkerBlockingQueue(workerQueueSize),
                    getThreadFactory(name, threadNamePrefix),
                    rejectedHandler == null ? new ThreadPoolExecutor.AbortPolicy() : rejectedHandler);

            ExecutorService existedPool = THREAD_MAP.putIfAbsent(name, pool);
            if (existedPool != null) {
                pool.shutdown();
            }
        }

        return THREAD_MAP.get(name);
    }

    /**
     * getThreadFactory
     *
     * @param name
     * @param threadNamePrefix
     * @return
     */
    public static ThreadFactory getThreadFactory(String name, String threadNamePrefix) {
        if (StringUtils.isBlank(threadNamePrefix)) {
            threadNamePrefix = name;
        }

        if (!threadNamePrefix.contains(STRING_PLACEHOLDER)) {
            threadNamePrefix += "_" + STRING_PLACEHOLDER;
        }

        return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix).build();
    }

    /**
     * getWorkerBlockingQueue
     *
     * @param workerQueueSize
     * @return
     */
    private static LinkedBlockingQueue<Runnable> getWorkerBlockingQueue(int workerQueueSize) {
        int queueMaxSize = workerQueueSize > 0 ? workerQueueSize : Integer.MAX_VALUE;
        return new LinkedBlockingQueue<>(queueMaxSize);
    }
}
