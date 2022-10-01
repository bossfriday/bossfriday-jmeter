package cn.bossfriday.jmeter;

import cn.bossfriday.jmeter.asserter.CodeMsgAsserter;
import cn.bossfriday.jmeter.asserter.ISamplerAsserter;
import cn.bossfriday.jmeter.common.PocException;
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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * SamplerAsserterHelper
 *
 * @author chenx
 */
public class AppSamplerAsserter {

    @Documented
    @Target({ElementType.TYPE})
    @Retention(RUNTIME)
    public @interface Asserter {
        /**
         * name
         *
         * @return
         */
        String name() default "";

        /**
         * ignore
         *
         * @return
         */
        boolean ignore() default false;
    }

    private static final Logger log = LogManager.getLogger(AppSamplerAsserter.class);
    private static Map<String, ISamplerAsserter> assertMap = null;
    private static String[] asserters;

    static {
        assertMap = new ConcurrentHashMap<>();
        Set<Class<?>> clzSet = new Reflections(CodeMsgAsserter.class.getPackageName()).getTypesAnnotatedWith(Asserter.class);
        for (Class<?> clazz : clzSet) {
            Asserter asserter = clazz.getAnnotation(Asserter.class);
            if (asserter.ignore()) {
                continue;
            }

            if (assertMap.containsKey(asserter.name())) {
                log.warn("duplicated asserter: {}", asserter.name());
            } else {
                try {
                    assertMap.put(asserter.name(), (ISamplerAsserter) clazz.getConstructor().newInstance());
                    log.info("load asserter: {} done", asserter.name());
                } catch (Exception e) {
                    log.error("load asserter error!", e);
                }
            }
        }

        asserters = new String[assertMap.size()];
        int x = 0;
        for (String key : assertMap.keySet()) {
            asserters[x] = key;
            x++;
        }
    }

    /**
     * getAsserter
     */
    public static ISamplerAsserter getAsserter(String name) throws PocException {
        if (!assertMap.containsKey(name)) {
            throw new PocException(String.format("unimplemented asserter: %s", name));
        }

        return assertMap.get(name);
    }

    /**
     * getAsserters
     *
     * @return
     */
    public static String[] getAsserters() {
        return asserters;
    }
}
