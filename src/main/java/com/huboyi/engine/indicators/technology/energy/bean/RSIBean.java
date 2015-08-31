package com.huboyi.engine.indicators.technology.energy.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

import com.huboyi.util.JAXBHelper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * RSI指标。
 * 
 * @author FrankTaylor <mailto:franktaylor@163.com>
 * @since 2015/01/28
 * @version 1.0
 */
@JsonPropertyOrder(value = {"date", "rsi"}, alphabetic = false)
public class RSIBean implements Serializable {

	private static final long serialVersionUID = 8426914477131635617L;

	/** 日期。*/
	@JsonProperty(value = "date", required = true)
	private Integer date = 19700101;
	
	/** rsi。*/
	@JsonProperty(value = "rsi", required = true)
	private BigDecimal rsi = BigDecimal.valueOf(0);
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append("[\n")
		.append("\t").append("date = ").append(date).append("\n")
		.append("\t").append("rsi = ").append(rsi).append("\n")
		.append("]\n");
		return builder.toString();
	}
	
	/**
	 * 把JavaBean转换为默认格式的JSON。 
	 * 
	 * @return String
	 * @throws JsonProcessingException
	 */
	public String toDefaultJson () 
	throws JsonProcessingException {
		return JAXBHelper.javaToDefaultJson(this);
	}
	
	/**
	 * 把JavaBean转换为mimi格式的JSON，建议在开发中采用此方法，以提升部分性能。 
	 * 
	 * @return String
	 * @throws JsonProcessingException
	 */
	public String toMiniJson () 
	throws JsonProcessingException {
		return JAXBHelper.javaToMiniJson(this);
	}
	
	/*---------------- 静态方法 ---------------*/
	
	/**
	 * 把JSON转换为JavaBean。
	 * 
	 * @param json json信息
	 * @return RSIBean
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static RSIBean 
	jsonToJava (String json) throws JsonParseException, JsonMappingException, IOException {
		return JAXBHelper.jsonToJava(json, RSIBean.class);
	}

	// --- get method and set method ---
	
	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public BigDecimal getRsi() {
		return rsi;
	}

	public void setRsi(BigDecimal rsi) {
		this.rsi = rsi;
	}
}