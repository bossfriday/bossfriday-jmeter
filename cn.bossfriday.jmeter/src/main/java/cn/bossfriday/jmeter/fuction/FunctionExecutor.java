package cn.bossfriday.jmeter.fuction;

import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.fuction.impl.RandomMobile;
import cn.bossfriday.jmeter.utils.AppSamplerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bossfriday.jmeter.common.Const.CONST_LF;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * FunctionExecutor
 *
 * @author chenx
 */
public class FunctionExecutor {

    private static final Logger log = LogManager.getLogger(FunctionExecutor.class);
    private static volatile FunctionExecutor instance;
    private Map<String, BaseFunction> functionMap = null;

    @Documented
    @Target({ElementType.TYPE})
    @Retention(RUNTIME)

    public @interface Fun {
        String name() default "";
    }

    private FunctionExecutor() {
        this.functionMap = new ConcurrentHashMap<>();
        Set<Class<?>> clzSet = new Reflections(RandomMobile.class.getPackageName()).getTypesAnnotatedWith(Fun.class);
        for (Class<?> clazz : clzSet) {
            Fun fun = clazz.getAnnotation(Fun.class);
            if (this.functionMap.containsKey(fun.name())) {
                log.warn("duplicated function: {}", fun.name());
                continue;
            }

            try {
                this.functionMap.put(fun.name(), (BaseFunction) clazz.getConstructor(String.class).newInstance(fun.name()));
                log.info("load function: {} done", fun.name());
            } catch (Exception e) {
                log.error("load function error!", e);
            }
        }
    }

    /**
     * getInstance
     *
     * @return
     */
    public static FunctionExecutor getInstance() {
        if (instance == null) {
            synchronized (FunctionExecutor.class) {
                if (instance == null) {
                    instance = new FunctionExecutor();
                }
            }
        }

        return instance;
    }

    /**
     * apply
     *
     * @param name
     * @param args
     * @return
     */
    public Object apply(String name, Object... args) throws PocException {
        BaseFunction function = this.getFunction(name);
        if (function == null) {
            throw new PocException(String.format("unimplemented function: %s", name));
        }

        Object result = function.apply(args);
        if (result instanceof Exception) {
            Exception ex = (Exception) result;
            ex.printStackTrace();
            throw new PocException(ex);
        }

        return result;
    }

    /**
     * getFunction
     */
    private BaseFunction getFunction(String name) {
        if (this.functionMap == null || this.functionMap.size() == 0) {
            return null;
        }

        if (!this.functionMap.containsKey(name)) {
            return null;
        }

        return this.functionMap.get(name);
    }

    /**
     * getDocument
     *
     * @return
     */
    public String getDocument() {
        Map<String, BaseFunction> sortedFuncMap = AppSamplerUtils.sortMapByKey(this.functionMap);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, BaseFunction> entry : sortedFuncMap.entrySet()) {
            BaseFunction function = entry.getValue();
            sb.append(function.getDocument() + CONST_LF);
        }

        return sb.toString();
    }
}
