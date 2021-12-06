package com.uav.base.dataBase;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 解析common.properties文件
 * 
 * @Title: CommonPropertiesUtil.java
 * @author maqian  
 * @date 2018年12月24日 上午11:09:15
 */
public class CommonPropertiesUtil extends PropertyPlaceholderConfigurer {

	/**
	 * 存储common.properties配置文件的key-value结果
	 */
	private static Properties commonPropertis = new Properties();

	/**
	 * 在项目应用数据库之前解析common.properties文件
	 * 1、构造driverUrl和alias
	 * 2、根据数据库名称确定当前项目的运行模式:（模式一：uav，模式二：uav_utmiss，模式三：uavhn）
	 * 
	 * @author maqian
	 * @date 2018年12月24日 上午11:06:51
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#processProperties(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.Properties)
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		String dbName = props.getProperty("dbName");
		String dbIp = props.getProperty("dbIp");
		String dbPort = props.getProperty("dbPort");
		String user = props.getProperty("user");
		String userPwd = props.getProperty("userPwd");
		String driverUrl = "jdbc:mysql://" + dbIp + ":" + dbPort + "/" + dbName + "?useSSL=false&user=" + user + "&password=" + userPwd
				+ "&characterEncoding=utf8";
		System.out.println("================>"+driverUrl);
		String alias = dbName;
		props.setProperty("driverUrl", driverUrl);
		props.setProperty("alias", alias);
		commonPropertis = props;
		super.processProperties(beanFactoryToProcess, props);
	}

	/**
	 * 根据key值获取相应的value
	 * 
	 * @Title: getProperty
	 * @author maqian
	 * @date 2018年12月24日 上午11:14:19
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return commonPropertis.getProperty(key);
	}

	/**
	 * 根据key值获取相应的value，如果value为null可设置默认值
	 * 
	 * @Title: getProperty
	 * @author maqian
	 * @date 2018年12月24日 上午11:14:36
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getProperty(String key, String defaultValue) {
		return commonPropertis.getProperty(key, defaultValue);
	}
}
