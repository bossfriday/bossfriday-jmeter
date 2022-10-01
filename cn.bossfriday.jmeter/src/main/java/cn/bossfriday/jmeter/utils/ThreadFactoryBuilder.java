package cn.bossfriday.jmeter.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreadFactoryBuilder
 *
 * @author chenx
 */
public class ThreadFactoryBuilder {

    private String nameFormat = null;
    private Boolean daemon = null;
    private Integer priority = null;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
    private ThreadFactory backingThreadFactory = null;

    /**
     * setNameFormat
     *
     * @param nameFormat
     * @return
     */
    public ThreadFactoryBuilder setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
        return this;
    }

    /**
     * setDaemon
     *
     * @param daemon
     * @return
     */
    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    /**
     * setPriority
     *
     * @param priority
     * @return
     */
    public ThreadFactoryBuilder setPriority(int priority) {
        this.checkArgument(priority >= Thread.MIN_PRIORITY,
                "Thread priority (%s) must be >= %s", priority, Thread.MIN_PRIORITY);
        this.checkArgument(priority <= Thread.MAX_PRIORITY,
                "Thread priority (%s) must be <= %s", priority, Thread.MAX_PRIORITY);
        this.priority = priority;
        return this;
    }

    /**
     * setUncaughtExceptionHandler
     *
     * @param uncaughtExceptionHandler
     * @return
     */
    public ThreadFactoryBuilder setUncaughtExceptionHandler(
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = checkNotNull(uncaughtExceptionHandler);

        return this;
    }

    /**
     * setThreadFactory
     *
     * @param backingThreadFactory
     * @return
     */
    public ThreadFactoryBuilder setThreadFactory(
            ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = checkNotNull(backingThreadFactory);

        return this;
    }

    /**
     * build
     *
     * @return
     */
    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        String nameFormat = builder.nameFormat;
        Boolean daemon = builder.daemon;
        Integer priority = builder.priority;
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null)
                ? builder.backingThreadFactory
                : Executors.defaultThreadFactory();
        AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;
        return runnable -> {
            Thread thread = backingThreadFactory.newThread(runnable);
            if (nameFormat != null) {
                thread.setName(String.format(nameFormat, count.getAndIncrement()));
            }

            if (daemon != null) {
                thread.setDaemon(daemon);
            }

            if (priority != null) {
                thread.setPriority(priority);
            }

            if (uncaughtExceptionHandler != null) {
                thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            }

            return thread;
        };
    }

    /**
     * checkArgument
     *
     * @param expression
     * @param errMsgTemplate
     * @param errorMessageArgs
     */
    private void checkArgument(boolean expression, String errMsgTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errMsgTemplate, errorMessageArgs));
        }
    }

    /**
     * checkNotNull
     *
     * @param reference
     * @param <T>
     * @return
     */
    private static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }

        return reference;
    }

}
