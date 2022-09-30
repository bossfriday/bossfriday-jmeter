package cn.bossfriday.jmeter.common;

/**
 * HttpMethod
 *
 * @author chenx
 */
public enum HttpMethod {

    /**
     * HttpMethods
     */
    GET("GET"),

    POST("POST"),

    HEAD("HEAD"),

    PUT("PUT"),

    OPTIONS("OPTIONS"),

    TRACE("TRACE"),

    DELETE("DELETE"),

    PATCH("PATCH");

    private String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
