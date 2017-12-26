/**
 * 
 */
package cn.touch.demo.util;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 21, 2017.
 */
public class ConfigUtils {
    protected static PropertiesConfiguration config;

    static {
    	try {
			config = new PropertiesConfiguration("env.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
