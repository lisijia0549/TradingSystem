package com.huboyi.data.load.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 监控载入股票行情数据的工作类。
 * 
 * @author FrankTaylor <mailto:franktaylor@163.com>
 * @since 2014/10/18
 * @version 1.0
 */
public class DataLoadMonitorTask implements Runnable {

	/** 日志。*/
	private static final Logger log = LogManager.getLogger(DataLoadMonitorTask.class);
	
	/** 需要载入行情数据的总个数。*/
	private final Integer marketDataFilepathNum;
	/** 当前已经载入的行情数据的个数。*/
	private final AtomicInteger currentReadMarketDataNum;
	
	/** 监控线程任务的执行控制信号，true：执行；false：停止。*/
	private boolean executionSignal;
	
	/**
	 * 构造函数。
	 * 
	 * @param marketDataFilepathMap 需要载入行情数据的总个数
	 * @param currentReadMarketDataNum 当前已经载入的行情数据的个数
	 */
	public DataLoadMonitorTask (Integer marketDataFilepathNum, AtomicInteger currentReadMarketDataNum) {
		this.marketDataFilepathNum = marketDataFilepathNum;
		this.currentReadMarketDataNum = currentReadMarketDataNum;
		
		this.executionSignal = true;
	}

	@Override
	public void run() {
		log.info("准备开启行情数据载入监控线程。");
		Thread current = Thread.currentThread();
		String name = current.getName();
		try {
			while (executionSignal) {
				BigDecimal fileNum = BigDecimal.valueOf(marketDataFilepathNum.intValue());
				BigDecimal readNum = BigDecimal.valueOf(currentReadMarketDataNum.get());
				
				BigDecimal rate = null;
				if (readNum.intValue() == 0) {
					rate = BigDecimal.valueOf(0);
				} else {
					rate = readNum.divide(fileNum, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));					
				}
				
				log.info("当前载入行情数据的进度为：" + rate.floatValue() + "%");
				TimeUnit.MILLISECONDS.sleep(1000);
			}
		} catch (Exception e) {
			log.error("[name = " + name + "]在监控行情数据载入的过程中出现错误！");
			log.error(e);
		}
	}
	
	/**
	 * 停止运行监控任务。
	 */
	public void shutdown() {
		this.executionSignal = false;
	}
	
}