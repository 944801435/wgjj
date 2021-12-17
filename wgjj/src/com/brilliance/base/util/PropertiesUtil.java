package com.brilliance.base.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesUtil {
	private static final Log log = LogFactory.getLog(PropertiesUtil.class);
	private static ClassLoader loader=null;
	
	public static final String DEFAULT_PROP_FILE="config.properties";
	
	static{
		try {
			loader = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException e) {
			PropertiesUtil propertiesUtil = new PropertiesUtil();
			loader = propertiesUtil.getClass().getClassLoader();
		}
	}
	
	public static String getPropertyValue(String resource, String name, String defaultValue) {
		String value = null;
		
		if (resource != null && resource.length() > 0 && name != null && name.length() > 0) {
			java.net.URL url = loader.getResource(resource);
			if (url == null)
				url = loader.getResource(resource + ".properties");
			if (url == null)
				url = ClassLoader.getSystemResource(resource);
			if (url == null)
				url = ClassLoader.getSystemResource(resource + ".properties");

			if (url == null) {
				log.error("not get resource is " + resource + "!");
			} else {
				InputStream in = null;
				try {
					in = url.openStream();
				} catch (Exception urlex) {
					log.error(urlex.getMessage());
				}
				if (in == null) {
					log.error("load resource is " + resource + " defeat!");
				} else {
					Properties properties = new Properties();
					try {
						properties.load(in);
						in.close();

						value = properties.getProperty(name);
					} catch (Exception e) {
						log.error("load properties resource is " + resource + " defeat!");
					}
				}
			}
		}
		
		return (value==null || value.length()==0) ? defaultValue : value.trim();
	}
	
	public static String getPropertyValue(String name, String defaultValue) {
		String value = null;
		String resource = DEFAULT_PROP_FILE;
		if (resource != null && resource.length() > 0 && name != null && name.length() > 0) {
			java.net.URL url = loader.getResource(resource);
			if (url == null)
				url = loader.getResource(resource + ".properties");
			if (url == null)
				url = ClassLoader.getSystemResource(resource);
			if (url == null)
				url = ClassLoader.getSystemResource(resource + ".properties");
			
			if (url == null) {
				log.error("not get resource is " + resource + "!");
			} else {
				InputStream in = null;
				try {
					in = url.openStream();
				} catch (Exception urlex) {
					log.error(urlex.getMessage());
				}
				if (in == null) {
					log.error("load resource is " + resource + " defeat!");
				} else {
					Properties properties = new Properties();
					try {
						properties.load(in);
						in.close();
						
						value = properties.getProperty(name);
					} catch (Exception e) {
						log.error("load properties resource is " + resource + " defeat!");
					}
				}
			}
		}
		
		return (value==null || value.length()==0) ? defaultValue : value.trim();
	}
	
	public static Map<String, String> getPropertyMap(String resource) {
        if(resource==null || resource.trim().length()<=0)
            return null;
        else
        	resource=resource.trim();
        
        ClassLoader loader=null;
        try {
          loader=Thread.currentThread().getContextClassLoader();
        } catch (SecurityException e) {
          PropertiesUtil propertiesUtil=new PropertiesUtil();
          loader=propertiesUtil.getClass().getClassLoader();
        }
        
        java.net.URL url=loader.getResource(resource);
        if(url==null)
            url=loader.getResource(resource+".properties");
        if(url==null)
            url=ClassLoader.getSystemResource(resource);
        if(url==null)
            url=ClassLoader.getSystemResource(resource + ".properties");
        if(url==null){
        	log.error("not get resource is "+resource+" !");
            return null;
        }
        
        InputStream in = null;
        try {
            in=url.openStream();
        } catch (Exception urlex) { }
        if (in==null){
        	log.error("load resource is "+resource+" dereat!");
            return null;
        }
        
        Properties properties=new Properties();
        try {
			properties.load(in);
			in.close();
		} catch (Exception e) {
			log.error("get properties resource is " + resource + " defeat!");
			return null;
		}
        
        try {
        	Set<?> entrySet=properties.entrySet();
            if(entrySet.size()>0){
            	Map<String, String> rtnMap=new HashMap<String, String>();
            	Iterator<?> iter=entrySet.iterator();
            	Entry<?, ?> entry=null;
            	while (iter.hasNext()) {
            		entry=(Entry<?, ?>)iter.next();
            		rtnMap.put((String)entry.getKey(), (String)entry.getValue());
    			}
            	return rtnMap;
            }else{
            	return null;
            }
        } catch (Exception e) {
			log.error("load resource entrys error!", e);
			return null;
		}
    }
}