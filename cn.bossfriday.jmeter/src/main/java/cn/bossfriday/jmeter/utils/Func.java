package cn.bossfriday.jmeter.utils;

/**
 * Func
 *
 * @author chenx
 */
public class Func {

    private Func() {

    }

    /**
     * Action0
     */
    public static interface Action0 {
        /**
         * invoke
         */
        public void invoke();
    }

    /**
     * Action1
     *
     * @param <T>
     */
    public static interface Action1<T> {
        /**
         * invoke
         *
         * @param arg
         */
        public void invoke(T arg);
    }

    /**
     * Action2
     *
     * @param <T1>
     * @param <T2>
     */
    public static interface Action2<T1, T2> {
        /**
         * invoke
         *
         * @param arg1
         * @param arg2
         */
        public void invoke(T1 arg1, T2 arg2);
    }

    /**
     * Action3
     *
     * @param <T1>
     * @param <T2>
     * @param <T3>
     */
    public static interface Action3<T1, T2, T3> {
        /**
         * invoke
         *
         * @param arg1
         * @param arg2
         * @param arg3
         */
        public void invoke(T1 arg1, T2 arg2, T3 arg3);
    }

    /**
     * Action4
     *
     * @param <T1>
     * @param <T2>
     * @param <T3>
     * @param <T4>
     */
    public static interface Action4<T1, T2, T3, T4> {
        /**
         * invoke
         *
         * @param arg1
         * @param arg2
         * @param arg3
         * @param arg4
         */
        public void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
    }

    /**
     * Function0
     *
     * @param <R>
     */
    public static interface Function0<R> {
        /**
         * invoke
         *
         * @return
         */
        public R invoke();
    }

    /**
     * Function1
     *
     * @param <T>
     * @param <R>
     */
    public static interface Function1<T, R> {
        /**
         * invoke
         *
         * @param arg
         * @return
         */
        public R invoke(T arg);
    }

    /**
     * Promise
     *
     * @param <V>
     */
    public static interface Promise<V> extends Action1<V> {
        /**
         * invokeWithThrowable
         *
         * @param e
         */
        public void invokeWithThrowable(Throwable e);
    }
}
