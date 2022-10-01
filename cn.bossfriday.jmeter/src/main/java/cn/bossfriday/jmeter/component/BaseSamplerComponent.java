package cn.bossfriday.jmeter.component;

import cn.bossfriday.jmeter.common.PocException;
import cn.bossfriday.jmeter.entities.SamplerSetting;
import lombok.Getter;
import lombok.Setter;

/**
 * 采样器元件
 *
 * @author chenx
 */
public abstract class BaseSamplerComponent {

    @Getter
    @Setter
    protected SamplerSetting setting;

    protected BaseSamplerComponent(SamplerSetting config) {
        this.setting = config;
    }

    /**
     * testStarted
     *
     * @throws PocException
     */
    public abstract void testStarted() throws PocException;

    /**
     * testEnded
     *
     * @throws PocException
     */
    public abstract void testEnded() throws PocException;
}
