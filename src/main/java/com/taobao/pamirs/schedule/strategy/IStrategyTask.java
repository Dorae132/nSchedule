package com.taobao.pamirs.schedule.strategy;

/**
 * 每个实例代表一个线程组，每个strategy可以对应IStrategyTask实例，来真正的处理配置的任务
 * @author Dorae
 *
 */
public interface IStrategyTask {
    public void initialTaskParameter(String strategyName, String taskParameter) throws Exception;

    /**
     * 停掉任务
     * @param strategyName
     * @throws Exception
     */
    public void stop(String strategyName) throws Exception;
}
