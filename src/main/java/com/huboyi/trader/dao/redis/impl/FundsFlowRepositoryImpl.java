//package com.huboyi.position.dao.redis.impl;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.UUID;
//
//import org.apache.log4j.Logger;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import com.huboyi.trader.entity.po.FundsFlowPO;
//import com.huboyi.trader.repository.FundsFlowRepository;
//
//
///**
// * 资金流水DAO。
// * 
// * @author FrankTaylor <mailto:franktaylor@163.com>
// * @since 1.0
// */
//public class FundsFlowRepositoryImpl extends RedisTemplate<String, FundsFlowPO> implements FundsFlowRepository {
//	
//	/** 日志。*/
//	private final Logger log = Logger.getLogger(FundsFlowRepositoryImpl.class);
//	
//	@Override
//	@Deprecated
//	public void createIndex(String stockCode) {
//		log.warn("当前数据库是Redis不用创建索引。");
//	}
//
//	@Override
//	public void insert(FundsFlowPO po) {
//		StringBuilder logMsg = new StringBuilder();
//		logMsg.append("invoke insert method").append("\n");
//		logMsg.append("@param [po = " + po + "]");
//		log.info(logMsg.toString());
//		
//		try {
//
//			// --- 把记录保存到Redis。
//			po.setId(UUID.randomUUID().toString() + "-" + getClass().getName());
//			opsForList().rightPush(getListName(po.getStockCode()), po);
//			
//			log.info("插入 [证券代码 = " + po.getStockCode() + "] 的资金流水记录成功。");
//		} catch (Throwable e) {
//			String errorMsg = "插入 [证券代码 = " + po.getStockCode() + "] 的资金流水记录失败!";
//			log.error(errorMsg, e);
//			throw new RuntimeException(errorMsg, e);
//		}
//	}
//	
//	@Override
//	public FundsFlowPO findNewOne(String stockCode) {
//		StringBuilder logMsg = new StringBuilder();
//		logMsg.append("invoke findNewOne method").append("\n");
//		logMsg.append("@param [stockCode = " + stockCode + "]");
//		log.info(logMsg.toString());
//		
//		try {
//
//			// --- 查询Redis。
//			List<FundsFlowPO> poList = findFundsFlowList(stockCode, null, null, null, null);
//			if (poList == null || poList.isEmpty()) {
//				return null;
//			}
//			
//			log.info("查询  [证券代码 = " + stockCode + "] 最近的一条资金流水记录成功。");
//			return poList.get(poList.size() - 1);
//		} catch (Throwable e) {
//			String errorMsg = "查询 [证券代码 = " + stockCode + "] 最近的一条资金流水记录失败!";
//			log.error(errorMsg, e);
//			throw new RuntimeException(errorMsg, e);
//		}
//	}
//
//	@Override
//	public List<FundsFlowPO> findFundsFlowList(String stockCode,
//			Integer beginTradeDate, Integer endTradeDate, Integer beginPage,
//			Integer endPage) {
//		StringBuilder logMsg = new StringBuilder();
//		logMsg.append("invoke findFundsFlowList method").append("\n");
//		logMsg.append("@param [stockCode = " + stockCode + "]").append("\n");
//		logMsg.append("@param [beginTradeDate = " + beginTradeDate + "]").append("\n");
//		logMsg.append("@param [endTradeDate = " + endTradeDate + "]").append("\n");
//		logMsg.append("@param [beginPage = " + beginPage + "]").append("\n");
//		logMsg.append("@param [endPage = " + endPage + "]").append("\n");
//		log.info(logMsg.toString());
//		
//		try {
//
//			// --- 查询Redis。
//			List<FundsFlowPO> poList = opsForList().range(getListName(stockCode), 0, -1);
//			
//			// --- 由于Redis没有其他数据库中的排序功能，这里需要自己实现按照trade_date升序。
//			Collections.sort(poList, new Comparator<FundsFlowPO>() {
//				@Override
//				public int compare(FundsFlowPO o1, FundsFlowPO o2) {
//					return (o1.getTradeDate() > o2.getTradeDate()) ? 1    :
//						   (o1.getTradeDate() < o2.getTradeDate()) ? -1   :
//				           0;
//				}
//			});
//			
//			// --- 把符合时间范围的记录载入集合。
//			List<FundsFlowPO> resultList = new ArrayList<FundsFlowPO>();
//			for (FundsFlowPO po : poList) {
//				
//				if (beginTradeDate != null && po.getTradeDate() < beginTradeDate) {
//					continue;
//				}
//				if (endTradeDate != null && po.getTradeDate() > endTradeDate) {
//					continue;
//				}
//				
//				resultList.add(po);
//			}
//			
//			// --- 对集合进行截取范围操作。
//			int fromIndex = (beginPage != null && beginPage > 0 && resultList.size() > beginPage) ? beginPage : 0;
//			int toIndex = (endPage != null && endPage > 0 && (resultList.size() - fromIndex) >= endPage) ? (endPage + fromIndex) : resultList.size();
//
//			log.info("按照条件查询 [证券代码 = " + stockCode + "] 的资金流水记录成功。");
//			return resultList.subList(fromIndex, toIndex);
//		} catch (Throwable e) {
//			String errorMsg = "查询 [证券代码 = " + stockCode + "] 最近的一条资金流水记录失败!";
//			log.error(errorMsg, e);
//			throw new RuntimeException(errorMsg, e);
//		}
//	}
//
//	@Override
//	public void dropCollection(String stockCode) {
//		StringBuilder logMsg = new StringBuilder();
//		logMsg.append("invoke dropCollection method").append("\n");
//		logMsg.append("@param [stockCode = " + stockCode + "]");
//		log.info(logMsg.toString());
//		
//		try {
//			// --- 把记录从Redis中删除。
//			delete(getListName(stockCode));
//			log.info("删除  [证券代码 = " + stockCode + "] 用于记录资金流水的集合成功。");
//		} catch (Throwable e) {
//			String errorMsg = "删除  [证券代码 = " + stockCode + "] 用于记录资金流水的集合失败!";
//			log.error(errorMsg, e);
//			throw new RuntimeException(errorMsg, e);
//		}
//	}
//	
//	/**
//	 * 得到集合名称。
//	 * 
//	 * @param stockCode 证券代码
//	 * @return String
//	 */
//	private String getListName(String stockCode) {
//		return "test" + ":" + "fundsFlow" + ":" + stockCode;
//	}
//}