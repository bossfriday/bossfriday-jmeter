package cn.bossfriday.jmeter.common;

/**
 * PocException
 *
 * @author chenx
 */
public class PocException extends Exception {

    private static final long serialVersionUID = 1L;

    public PocException(Exception e) {
        super(e);
    }

    public PocException(String msg) {
        super(msg);
    }

    public PocException(String msg, Exception e) {
        super(msg, e);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
