package com.utry.openticket.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.util.TypeUtils;

/**
 * java读取配置文件
 *
 * @author Neo 2017-5-12
 * @version 1.1
 * 依赖于:fastjson-1.2.7.jar
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PropertiesUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static Properties properties;

    private static final String PROPERTIES_EGIS_FILE_NAME = "changeChina.properties";

    static {
        properties = new Properties();
        InputStream scmsStream = null;
        try {
            scmsStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_EGIS_FILE_NAME);
            properties.load(scmsStream);
            logger.info("PropertiesUtils", "staitc init prop", properties.toString());

        } catch (Exception e) {
        } finally {
            try {
                if (scmsStream != null) {
                    scmsStream.close();
                }
            } catch (Exception e) {

            }
        }
    }

    public static String getProperty(String key) {
        String result = properties.getProperty(key);
        return result;
    }

    public static String getProperty(String key, String defaultValue) {
        String result = properties.getProperty(key, defaultValue);
        return result;
    }

    public static String getProperties(String propertiesName, String key) {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(propertiesName);
            props.load(inputStream);
        } catch (IOException e) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {

            }
        }

        return props.getProperty(key);
    }

    public static Object getObject(String key, Class clazz) {
        Object obj = getProperty(key);
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public static Boolean getBoolean(String key) {
        Object value = getProperty(key);
        if (value == null)
            return null;
        else
            return TypeUtils.castToBoolean(value);
    }

    public static byte[] getBytes(String key) {
        Object value = getProperty(key);
        if (value == null)
            return null;
        else
            return TypeUtils.castToBytes(value);
    }

    public static boolean getBooleanValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return false;
        else
            return TypeUtils.castToBoolean(value).booleanValue();
    }

    public static Byte getByte(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToByte(value);
    }

    public static byte getByteValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return 0;
        else
            return TypeUtils.castToByte(value).byteValue();
    }

    public static Short getShort(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToShort(value);
    }

    public static short getShortValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return 0;
        else
            return TypeUtils.castToShort(value).shortValue();
    }

    public static Integer getInteger(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToInt(value);
    }

    public static int getIntValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return 0;
        else
            return TypeUtils.castToInt(value).intValue();
    }

    public static Long getLong(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToLong(value);
    }

    public static long getLongValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return 0L;
        else
            return TypeUtils.castToLong(value).longValue();
    }

    public static Float getFloat(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToFloat(value);
    }

    public static float getFloatValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return 0.0F;
        else
            return TypeUtils.castToFloat(value).floatValue();
    }

    public static Double getDouble(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToDouble(value);
    }

    public static double getDoubleValue(String key) {
        Object value = getProperty(key);
        if (value == null)
            return 0.0D;
        else
            return TypeUtils.castToDouble(value).doubleValue();
    }

    public static BigDecimal getBigDecimal(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToBigDecimal(value);
    }

    public static BigInteger getBigInteger(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToBigInteger(value);
    }

    public static String getString(String key) {
        Object value = getProperty(key);
        if (value == null)
            return null;
        else
            return value.toString();
    }

    public static Date getDate(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToDate(value);
    }

    public static java.sql.Date getSqlDate(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToSqlDate(value);
    }

    public static Timestamp getTimestamp(String key) {
        Object value = getProperty(key);
        return TypeUtils.castToTimestamp(value);
    }

    public static void main(String[] args) {
        System.out.println(getProperty("mobile.url"));
    }
}
