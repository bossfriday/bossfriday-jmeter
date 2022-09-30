package cn.bossfriday.jmeter;

import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import cn.bossfriday.jmeter.sampler.BaseSampler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * AppSamplerBuilder
 *
 * @author chenx
 */
public class AppSamplerBuilder {

    @Documented
    @Target({ElementType.TYPE})
    @Retention(RUNTIME)
    public @interface SamplerType {
        /**
         * 采样器类型名称
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

    private static final Logger log = LogManager.getLogger(AppSamplerBuilder.class);
    private static ConcurrentHashMap<String, Class<?>> clazzMap = new ConcurrentHashMap<>();
    private static String[] samplerTypes;

    static {
        // init clazzMap
        Set<Class<? extends BaseSampler>> classes = new Reflections().getSubTypesOf(BaseSampler.class);
        for (Class<?> clazz : classes) {
            SamplerType samplerType = clazz.getAnnotation(SamplerType.class);
            if (samplerType.ignore()) {
                continue;
            }

            if (!clazzMap.containsKey(samplerType.name())) {
                clazzMap.put(samplerType.name(), clazz);
                log.info("load sampler done: {}", samplerType.name());
            } else {
                log.warn("duplicated sampler: {}", samplerType.name());
            }
        }

        // init samplerTypeNames
        samplerTypes = new String[clazzMap.size()];
        int x = 0;
        for (String key : clazzMap.keySet()) {
            samplerTypes[x] = key;
            x++;
        }

        // sort
        int size = samplerTypes.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < samplerTypes.length; j++) {
                if (samplerTypes[i].compareTo(samplerTypes[j]) > 0) {
                    String temp = samplerTypes[i];
                    samplerTypes[i] = samplerTypes[j];
                    samplerTypes[j] = temp;
                }
            }
        }
    }

    /**
     * getSampler
     */
    public static BaseSampler getSampler(SamplerSetting config)
            throws PocException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String samplerTypeName = config.getSamplerType();
        if (!clazzMap.containsKey(config.getSamplerType())) {
            throw new PocException("invalid samplerTypeName!(" + samplerTypeName + ")");
        }

        return (BaseSampler) clazzMap.get(samplerTypeName).getConstructor(SamplerSetting.class).newInstance(config);
    }

    /**
     * getSamplerTypes
     *
     * @return
     */
    public static String[] getSamplerTypes() {
        return samplerTypes;
    }
}
