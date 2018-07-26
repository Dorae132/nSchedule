package com.taobao.pamirs.schedule.test;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.strategy.TBScheduleManagerFactory;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;

/**
 * 测试工具类
 * @author Dorae
 *
 */
public class Start {

	public static final Logger log = LoggerFactory.getLogger(Start.class);
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new FileSystemXmlApplicationContext("schedule.xml");
		
		// 初始化调度器工厂
		TBScheduleManagerFactory scheduleManagerFactory = new TBScheduleManagerFactory();
		
		Properties properties = new Properties();
		properties.put("zkConnectString", "127.0.0.1:10001");
        properties.put("rootPath", "/taobao-schedule/train_worker");
        properties.put("zkSessionTimeout", "60000"); 
        properties.put("userName", "train_dev");
        properties.put("password", " train_dev ");
        properties.put("isCheckParentPath", "true");
        
        scheduleManagerFactory.setApplicationContext(context);
        scheduleManagerFactory.init(properties);
        
        // 创建任务调度的基本信息
		String baseTaskTypeName = "DemoTask";
		ScheduleTaskType baseTaskType = new ScheduleTaskType();
		baseTaskType.setBaseTaskType(baseTaskTypeName);
		baseTaskType.setDealBeanName("demoTaskBean");
		baseTaskType.setHeartBeatRate(10000);
		baseTaskType.setJudgeDeadInterval(100000);
		baseTaskType.setTaskParameter("AREA=BJ,YEAR>30");
		baseTaskType.setTaskItems(ScheduleTaskType
				.splitTaskItem("0:{TYPE=A,KIND=1},1:{TYPE=A,KIND=2},2:{TYPE=A,KIND=3},3:{TYPE=A,KIND=4},"
						+ "4:{TYPE=A,KIND=5},5:{TYPE=A,KIND=6},6:{TYPE=A,KIND=7},7:{TYPE=A,KIND=8},"
						+ "8:{TYPE=A,KIND=9},9:{TYPE=A,KIND=10}"));
		baseTaskType.setFetchDataNumber(500);
		baseTaskType.setThreadNumber(5);
		scheduleManagerFactory.getScheduleDataManager().createBaseTaskType(baseTaskType);
		log.info("创建调度任务success! " + baseTaskType.toString());
		
		// 创建任务调度策略
		String taskName = baseTaskTypeName;
		String strategyName = taskName + "-Strategy";
		scheduleManagerFactory.getScheduleStrategyManager().deleteMachineStrategy(strategyName, true);
		ScheduleStrategy strategy = new ScheduleStrategy();
		strategy.setStrategyName(strategyName);
		strategy.setKind(ScheduleStrategy.Kind.Schedule);
		strategy.setTaskName(taskName);
		strategy.setTaskParameter("china");
		
		strategy.setNumOfSingleServer(1);
        strategy.setAssignNum(10);
        strategy.setIPList("127.0.0.1".split(","));
        scheduleManagerFactory.getScheduleStrategyManager().createScheduleStrategy(strategy);
        log.info("创建调度策略success！ " + strategy.toString());
	}
}
