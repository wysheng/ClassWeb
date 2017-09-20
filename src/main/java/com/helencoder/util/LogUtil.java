package com.helencoder.util;

import org.apache.log4j.Logger;

/**
 * 日志记录统一配置
 *
 * Created by helencoder on 2017/9/20.
 */
public class LogUtil {
    private static Logger logger = Logger.getLogger(LogUtil.class);

    public static void main(String[] args) {

//        // 手动加载.properties文件
//        PropertyConfigurator.configure(configFilePath);

        // 记录debug级别的信息
        logger.debug("This is debug message.");
        // 记录info级别的信息
        logger.info("This is info message.");
        // 记录error级别的信息
        logger.error("This is error message.");
    }
}
